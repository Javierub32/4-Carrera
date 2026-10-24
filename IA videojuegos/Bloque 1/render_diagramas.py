"""Produce diagramas SVG y traza legible a partir de ida_modelo.py."""
from __future__ import annotations

import heapq
import html
import json
from collections import defaultdict
from pathlib import Path

BASE = Path(__file__).parent
OUT = BASE / "estudio-ia"
data = json.loads((OUT / "traza-ida.json").read_text(encoding="utf-8"))
edges, trace = data["aristas"], data["traza"]
X = {1: 0, 2: 500, 3: 900, 4: 1300, 5: 1800}
Y = {1: 0, 2: 600, 3: 1200, 4: 1700, 5: 2100}
START, GOAL = (1, 1), (5, 5)
POS = lambda n: (160 + (n[1] - 1) * 220, 140 + (5 - n[0]) * 185)
N = lambda n: f"({n[0]},{n[1]})"


def svg_doc(width, height, content):
    return (f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}" '
            f'viewBox="0 0 {width} {height}" role="img">'
            '<style>text{font-family:Arial,sans-serif;fill:#182230} '
            '.edge{fill:none;stroke-width:3} .node{fill:#fff;stroke:#273443;stroke-width:2}'
            '</style><defs><marker id="arrow" viewBox="0 0 10 10" refX="9" refY="5" '
            'markerWidth="9" markerHeight="9" orient="auto-start-reverse">'
            '<path d="M0 0 L10 5 L0 10 Z" fill="context-stroke"/></marker></defs>'
            + content + '</svg>')


def network(highlight=False, heuristic=False):
    solution = [tuple(n) for n in trace[-1]["solucion"]]
    pairs = set(zip(solution, solution[1:]))
    parts = [f'<rect width="1220" height="1020" fill="#f9fafb"/>',
             f'<text x="35" y="42" font-size="27" font-weight="700">'
             + ('Camino óptimo · 2900 s' if highlight else 'Red dirigida y tiempos' if not heuristic else 'Heurística h(n) en segundos')
             + '</text>']
    if not heuristic:
        parts.append('<text x="35" y="70" font-size="15">Etiqueta: longitud m / velocidad m·s⁻¹ = tiempo s. '
                     'Gruesa: 2 m/s; horizontal: 2 m/s; punteada: 1 m/s; vertical: 1 m/s.</text>')
    else:
        parts.append('<text x="35" y="70" font-size="15">h(n) = (|1800 − x| + |2100 − y|) / 2; '
                     'x e y son metros desde (1,1).</text>')
    for e in edges:
        a, b = tuple(e["a"]), tuple(e["b"])
        xa, ya = POS(a); xb, yb = POS(b)
        on_path = (a, b) in pairs
        color = '#15803d' if highlight and on_path else '#cbd5e1' if highlight or heuristic else '#2563eb' if e['tipo']=='punteada' else '#334155'
        width = 6 if highlight and on_path else 4 if e['tipo']=='gruesa' and not heuristic else 2.5
        # Keep endpoints clear of circular nodes.
        dx, dy = xb-xa, yb-ya
        length = (dx*dx+dy*dy)**0.5
        ux, uy = dx/length, dy/length
        x1,y1 = xa+ux*24, ya+uy*24
        x2,y2 = xb-ux*26, yb-uy*26
        dash = ' stroke-dasharray="8 6"' if e['tipo']=='punteada' and not on_path else ''
        title = f"{N(a)} → {N(b)}: {e['longitud_m']} m / {e['velocidad_m_s']} m/s = {e['coste_s']} s ({e['tipo']})"
        parts.append(f'<path class="edge" d="M{x1:.1f} {y1:.1f} L{x2:.1f} {y2:.1f}" '
                     f'stroke="{color}" stroke-width="{width}" marker-end="url(#arrow)"{dash}>'
                     f'<title>{html.escape(title)}</title></path>')
        if not heuristic and (not highlight or on_path):
            xm, ym = (xa+xb)/2, (ya+yb)/2
            if abs(dx)>0:
                offset = -12 if e['tipo']!='punteada' else 22
                ym += offset
            else:
                xm += 15 if e['tipo']!='punteada' else -80
            label=f"{e['longitud_m']}/{e['velocidad_m_s']}={e['coste_s']}"
            parts.append(f'<text x="{xm:.0f}" y="{ym:.0f}" text-anchor="middle" '
                         f'font-size="13" font-weight="600" fill="{color}">{label}</text>')
    nodes = sorted(set(tuple(e['a']) for e in edges)|set(tuple(e['b']) for e in edges))
    for n in nodes:
        x,y=POS(n)
        fill='#bbf7d0' if highlight and n in solution else '#fee2e2' if n==START else '#dcfce7' if n==GOAL else '#fff'
        parts.append(f'<circle class="node" cx="{x}" cy="{y}" r="22" fill="{fill}"/>')
        parts.append(f'<text x="{x}" y="{y+4}" text-anchor="middle" font-size="12" font-weight="700">{N(n)}</text>')
        if heuristic:
            h=(1800-X[n[1]]+2100-Y[n[0]])//2
            parts.append(f'<text x="{x}" y="{y+43}" text-anchor="middle" font-size="13" '
                         f'font-weight="700">h={h}</text>')
    if highlight:
        parts.append('<text x="35" y="970" font-size="18" font-weight="700">'
                     '(1,1) → (2,1) → (2,2) → (2,3) → (2,5) → (3,5) → (4,5) → (5,5)'
                     '</text>')
    return svg_doc(1220,1020,''.join(parts))


(OUT/'01-red.svg').write_text(network(),encoding='utf-8')
(OUT/'02-heuristica.svg').write_text(network(heuristic=True),encoding='utf-8')
(OUT/'10-camino-final.svg').write_text(network(highlight=True),encoding='utf-8')


def tree_svg(t):
    recs=t['registros']
    by_id={r['id']:r for r in recs}
    children=defaultdict(list)
    for r in recs:
        if r['parent'] is not None: children[r['parent']].append(r['id'])
    depths={0:0}
    for r in recs[1:]: depths[r['id']]=depths[r['parent']]+1
    leaf_counter=[0]
    xpos={}
    def assign(i):
        if not children[i]:
            xpos[i]=leaf_counter[0]*205+120
            leaf_counter[0]+=1
        else:
            vals=[assign(j) for j in children[i]]
            xpos[i]=sum(vals)/len(vals)
        return xpos[i]
    assign(0)
    width=max(800,leaf_counter[0]*205+120)
    height=max(depths.values())*125+220
    parts=[f'<rect width="{width}" height="{height}" fill="#f8fafc"/>',
           f'<text x="25" y="32" font-size="23" font-weight="700">'
           f'IDA* · iteración {t["iteracion"]} · umbral {t["umbral"]} s</text>',
           '<text x="25" y="56" font-size="13">Azul: expansión · rojo: poda por f &gt; umbral · verde: objetivo. '
           'Orden: norte, este, sur, oeste.</text>']
    for r in recs[1:]:
        p=by_id[r['parent']]
        x1,y1=xpos[p['id']],125+depths[p['id']]*125
        x2,y2=xpos[r['id']],125+depths[r['id']]*125
        parts.append(f'<path d="M{x1} {y1+30} L{x2} {y2-30}" stroke="#94a3b8" '
                     'stroke-width="2" fill="none"/>')
    for r in recs:
        x,y=xpos[r['id']],125+depths[r['id']]*125
        color={'expande':'#dbeafe','poda':'#fee2e2','objetivo':'#bbf7d0'}[r['estado']]
        stroke={'expande':'#2563eb','poda':'#dc2626','objetivo':'#15803d'}[r['estado']]
        path=' → '.join(N(n) for n in r['camino'])
        parts.append(f'<g><title>{html.escape(path)}</title><rect x="{x-77}" y="{y-32}" '
                     f'width="154" height="64" rx="8" fill="{color}" stroke="{stroke}" stroke-width="2"/>'
                     f'<text x="{x}" y="{y-11}" text-anchor="middle" font-size="15" font-weight="700">'
                     f'{N(r["nodo"])}</text><text x="{x}" y="{y+8}" text-anchor="middle" font-size="12">'
                     f'g={r["g"]} · h={r["h"]} · f={r["f"]}</text>'
                     f'<text x="{x}" y="{y+25}" text-anchor="middle" font-size="11" '
                     f'fill="{stroke}">{r["estado"]}</text></g>')
    return svg_doc(width,height,''.join(parts))


for t in trace:
    (OUT/f'ida-umbral-{t["umbral"]}.svg').write_text(tree_svg(t),encoding='utf-8')

lines=['# Traza completa de IDA*','','🟡 Calculada a partir de la red del enunciado. En cada fila se muestra '
       'el camino **actual**, g, h y f en segundos. Los nodos repetidos en el camino actual se omiten. '
       'Se recorren los sucesores en orden norte, este, sur, oeste.','','']
for t in trace:
    lines.extend([f'## Iteración {t["iteracion"]}: umbral {t["umbral"]} s','',
        f'![Árbol IDA* con umbral {t["umbral"]} s](ida-umbral-{t["umbral"]}.svg)','',
        '| Paso | Camino actual | g | h | f | Resultado |',
        '| ---: | --- | ---: | ---: | ---: | --- |'])
    for i,r in enumerate(t['registros'],1):
        path=' → '.join(N(n) for n in r['camino'])
        lines.append(f'| {i} | {path} | {r["g"]} | {r["h"]} | {r["f"]} | {r["estado"]} |')
    lines.extend(['',f'Siguiente umbral: **{t["siguiente"] if t["solucion"] is None else "no procede; objetivo encontrado"}** '
                  +('s (mínimo f podado).' if t['solucion'] is None else ''),''])
(OUT/'traza-completa.md').write_text('\n'.join(lines),encoding='utf-8')

street_lines = ['# Calles dirigidas: longitudes, velocidades y tiempos', '',
                '🟢 Las flechas, longitudes entre filas y columnas y tipos de trazo proceden de '
                '[enunciadoIDA.png](../enunciadoIDA.png). 🟡 Cada tiempo se ha calculado como '
                '`longitud / velocidad`. Las filas y columnas del enunciado van de 1 a 5.', '',
                '| Calle dirigida | Tipo | Longitud (m) | Velocidad (m/s) | Tiempo (s) |',
                '| --- | --- | ---: | ---: | ---: |']
for e in edges:
    street_lines.append(f'| {N(e["a"])} → {N(e["b"])} | {e["tipo"]} | '
                        f'{e["longitud_m"]} | {e["velocidad_m_s"]} | {e["coste_s"]} |')
street_lines.extend(['', 'Las calles largas se representan como un solo arco cuando no hay cruce '
                     'intermedio: por ejemplo, `(2,3)→(2,5)` mide `400+500=900 m`, '
                     'y `(3,1)→(5,1)` mide `500+400=900 m`.'])
(OUT/'tabla-calles.md').write_text('\n'.join(street_lines),encoding='utf-8')

nodes=sorted(set(tuple(e['a']) for e in edges)|set(tuple(e['b']) for e in edges))
heur_lines=['# Valores de la heurística', '',
            '🟡 `h(r,c)=(3900−x_c−y_r)/2` segundos. Los nodos son los cruces reales '
            'del esquema; no se añaden cruces en puntos donde una calle pasa de largo.', '',
            '| Nodo (fila,columna) | x (m) | y (m) | h (s) |',
            '| --- | ---: | ---: | ---: |']
for r,c in nodes:
    x,y=X[c],Y[r]
    heur_lines.append(f'| ({r},{c}) | {x} | {y} | {(3900-x-y)//2} |')
(OUT/'tabla-heuristica.md').write_text('\n'.join(heur_lines),encoding='utf-8')

# Verificación independiente de mínimo coste con Dijkstra.
adj=defaultdict(list)
for e in edges: adj[tuple(e['a'])].append((tuple(e['b']),e['coste_s']))
heap=[(0,START)]
best={START:0}
while heap:
    cost,node=heapq.heappop(heap)
    if cost!=best[node]: continue
    if node==GOAL: break
    for nxt,w in adj[node]:
        if cost+w<best.get(nxt,10**18):
            best[nxt]=cost+w
            heapq.heappush(heap,(cost+w,nxt))
assert best[GOAL]==2900,best[GOAL]
print('Dijkstra independiente:',best[GOAL],'s; diagramas:',len(trace)+3)

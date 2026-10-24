"""Reconstrucción verificable del ejercicio IDA* de Bloque 1."""
from __future__ import annotations

import json
from collections import defaultdict
from pathlib import Path

OUT = Path(__file__).parent / "estudio-ia"
OUT.mkdir(exist_ok=True)
X = {1: 0, 2: 500, 3: 900, 4: 1300, 5: 1800}
Y = {1: 0, 2: 600, 3: 1200, 4: 1700, 5: 2100}
START, GOAL = (1, 1), (5, 5)
edges = []


def add(a, b, kind):
    r1, c1 = a
    r2, c2 = b
    length = abs(X[c2] - X[c1]) + abs(Y[r2] - Y[r1])
    speed = 1 if kind in {"punteada", "vertical"} else 2
    assert length and length % speed == 0
    edges.append({"a": a, "b": b, "tipo": kind, "longitud_m": length,
                  "velocidad_m_s": speed, "coste_s": length // speed})


# Carreteras horizontales, de izquierda a derecha en el dibujo.
for a, b, kind in [
    ((5, 2), (5, 1), "gruesa"), ((5, 3), (5, 2), "horizontal"),
    ((5, 4), (5, 3), "punteada"), ((5, 5), (5, 4), "horizontal"),
    ((4, 2), (4, 3), "horizontal"), ((4, 3), (4, 4), "punteada"),
    ((4, 4), (4, 5), "horizontal"),
    ((3, 2), (3, 1), "horizontal"), ((3, 3), (3, 2), "horizontal"),
    ((3, 5), (3, 3), "gruesa"),
    ((2, 1), (2, 2), "horizontal"), ((2, 2), (2, 3), "punteada"),
    ((2, 3), (2, 5), "gruesa"),
    ((1, 2), (1, 1), "horizontal"), ((1, 3), (1, 2), "punteada"),
    ((1, 5), (1, 3), "horizontal"),
]:
    add(a, b, kind)

# Carreteras verticales, de abajo arriba salvo las flechas descendentes.
for a, b, kind in [
    ((1, 1), (2, 1), "vertical"), ((2, 1), (3, 1), "vertical"),
    ((3, 1), (5, 1), "gruesa"),
    ((5, 2), (4, 2), "gruesa"), ((4, 2), (3, 2), "gruesa"),
    ((3, 2), (2, 2), "vertical"), ((2, 2), (1, 2), "punteada"),
    ((1, 3), (2, 3), "punteada"), ((2, 3), (3, 3), "gruesa"),
    ((3, 3), (4, 3), "vertical"), ((4, 3), (5, 3), "punteada"),
    ((5, 4), (4, 4), "punteada"),
    ((1, 5), (2, 5), "vertical"), ((2, 5), (3, 5), "gruesa"),
    ((3, 5), (4, 5), "vertical"), ((4, 5), (5, 5), "vertical"),
]:
    add(a, b, kind)

adj = defaultdict(list)
for e in edges:
    adj[tuple(e["a"])].append(e)


def heuristic(n):
    r, c = n
    return (X[5] - X[c] + Y[5] - Y[r]) // 2


def direction(a, b):
    if b[0] > a[0]: return 0  # Norte
    if b[1] > a[1]: return 1  # Este
    if b[0] < a[0]: return 2  # Sur
    return 3  # Oeste


for node in adj:
    adj[node].sort(key=lambda e: direction(node, tuple(e["b"])))

trace = []
threshold = heuristic(START)
for iteration in range(1, 100):
    records = []
    next_threshold = [float("inf")]
    solution = [None]

    def visit(node, g, path, parent, via=None):
        h = heuristic(node)
        f = g + h
        rec_id = len(records)
        status = "poda" if f > threshold else "objetivo" if node == GOAL else "expande"
        records.append({"id": rec_id, "parent": parent, "nodo": node,
                        "g": g, "h": h, "f": f, "estado": status,
                        "camino": path, "via": via})
        if f > threshold:
            next_threshold[0] = min(next_threshold[0], f)
            return
        if node == GOAL:
            solution[0] = path
            return
        for e in adj[node]:
            child = tuple(e["b"])
            if child in path:
                continue
            visit(child, g + e["coste_s"], path + [child], rec_id, e)
            if solution[0]:
                return

    visit(START, 0, [START], None)
    trace.append({"iteracion": iteration, "umbral": threshold,
                  "siguiente": None if next_threshold[0] == float("inf") else next_threshold[0],
                  "solucion": solution[0], "registros": records})
    if solution[0] or next_threshold[0] == float("inf"):
        break
    threshold = next_threshold[0]

with (OUT / "traza-ida.json").open("w", encoding="utf-8") as fp:
    json.dump({"aristas": edges, "traza": trace}, fp, ensure_ascii=False, indent=2)

for t in trace:
    print(f"IT {t['iteracion']} UMBRAL {t['umbral']} SIGUIENTE {t['siguiente']} "+
          f"EXPANDIDOS {sum(r['estado']=='expande' for r in t['registros'])} "+
          f"PODAS {sum(r['estado']=='poda' for r in t['registros'])} "+
          f"SOLUCION {t['solucion']}")
    for r in t["registros"]:
        print("  " + " -> ".join(str(x) for x in r["camino"]) +
              f"  g={r['g']} h={r['h']} f={r['f']} {r['estado']}")

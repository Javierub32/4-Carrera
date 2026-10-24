# Las dos clases que faltan: lectura y explicación

He inspeccionado las **dos fotografías completas**. 🟢 es una lectura segura del manuscrito; 🟢❓ es una lectura dudosa que no usaré como dato; 🟡 es una reconstrucción o explicación mía. Puedes abrir las fotos originales: [claseFaltante1.jpeg](../claseFaltante1.jpeg) y [claseFaltante2.jpeg](../claseFaltante2.jpeg).

## Foto 1 · lectura de los apuntes

🟢 El encabezado es **«Backtrack acotado»**. Debajo se menciona poner una cota a la profundidad y declarar fracaso si no se alcanza el objetivo dentro de ella. Hay una línea tachada entre este encabezado y el siguiente; no la tomo como regla vigente. (claseFaltante1.jpeg)

🟢❓ En la frase entre paréntesis aparece algo semejante a «prof ... cota», pero las letras centrales no se distinguen. La fotografía no establece con claridad si habla de una cota superior sola o de dos cotas. (claseFaltante1.jpeg)

🟢 El segundo encabezado parece **«BSID»** seguido de *Backtrack Iterative Deepening*. La sigla manuscrita es dudosa; el texto en inglés y la frase siguiente sí indican búsquedas repetidas con profundidad creciente «hasta solución con p=d», donde `p` parece denotar el límite y `d` la profundidad de la solución. (claseFaltante1.jpeg)

🟢 Aparece «Ejemplo: Trasvase de jarras». Hay una jarra `G=5` litros y una `P=2` litros. El estado se escribe `(G,P)`, con `0≤G≤5` y `0≤P≤2`. Se lee estado de salida `(3,2)` y condición objetivo `G=1 ∨ P=1`. Se agrupan acciones de **vaciar**, **trasvase total** y **trasvase parcial**; no veo una acción de llenar desde un grifo en esta fotografía. (claseFaltante1.jpeg)

🟢 Las dos acciones de vaciar se leen `(G,P)→(0,P)` si `G>0` y `(G,P)→(G,0)` si `P>0`. Las fórmulas de trasvase total representan vaciar completamente la jarra emisora cuando cabe en la receptora. Las de trasvase parcial representan llenar la receptora y dejar el resto en la emisora. (claseFaltante1.jpeg)

🟢❓ Las condiciones escritas al lado de la fórmula `G→P` parcial parecen incluir `G+P>2`, y las de `P→G` parcial `G+P>5`, pero el trazo de los signos y algún sumando es tenue. En el extremo inferior, algunas ramas y números de la exploración no son legibles; solo se distinguen con seguridad el arranque `(3,2)`, estados como `(0,2)`, `(3,0)`, `(5,0)` y un objetivo `(1,2)` enmarcado. (claseFaltante1.jpeg)

🟢❓ Junto al árbol se ve una anotación de **orden de operadores** que parece una secuencia de abreviaturas, pero no se puede leer completa ni inequívocamente. No la usaré para fijar un orden universal de sucesores. (claseFaltante1.jpeg)

## Foto 1 · ahora entendamos lo que ocurre

🟡 **Backtracking** significa probar una acción, seguir por esa rama y, si no sirve, volver al último punto con alternativas. Una búsqueda en profundidad sin límite puede quedarse para siempre en un ciclo; por eso una cota de profundidad interrumpe cada rama al llegar al límite. Si no encuentra solución bajo esa cota, el fracaso solo dice «no hay solución a esa profundidad o menor», no que no exista una solución más profunda. Esto conecta con el PDF, donde backtracking no es completo en general y guarda el camino actual. (Contraste con Tema 1.pdf pp.9,18)

🟡 La **profundización iterativa** resuelve el problema de elegir una cota de antemano. Ejecuta profundidad limitada con `L=0`, luego `L=1`, `L=2`, etc. En un árbol de ramificación finita, si la primera solución está a profundidad `d`, la pasada `L=d` la alcanzará. Si cada movimiento cuesta lo mismo, esa primera profundidad también minimiza el coste. Con costes distintos, minimizar pasos no minimiza tiempo: la foto 2 escribe correctamente «admisible: no, en general». (Contraste con claseFaltante2.jpeg y Tema 1.pdf p.10)

🟡 Para las jarras, no confundas **capacidad** con **contenido**. `(3,2)` dice que la grande contiene 3 litros de un máximo de 5 y la pequeña 2 de un máximo de 2. Si vacías la pequeña, obtienes `(3,0)`. Si pasas 2 litros de la grande a la pequeña, obtienes `(1,2)`, que satisface `G=1`. Es un camino de **dos acciones**: `(3,2) → (3,0) → (1,2)`. La primera foto parece enmarcar precisamente ese resultado. (Derivado de las capacidades y acciones de claseFaltante1.jpeg)

🟡 Las cuatro reglas de trasvase quedan más limpias si se separa el caso «cabe todo» del caso «se llena antes». Desde `G` hacia `P`:

| Caso | Condición suficiente | Nuevo estado | Ejemplo |
| --- | --- | --- | --- |
| Cabe toda `G` | `G>0`, `P<2`, `G+P≤2` | `(0,G+P)` | `(1,0)→(0,1)` |
| Se llena `P` | `G>0`, `P<2`, `G+P>2` | `(G+P−2,2)` | `(3,0)→(1,2)` |

🟡 Y desde `P` hacia `G`:

| Caso | Condición suficiente | Nuevo estado | Ejemplo |
| --- | --- | --- | --- |
| Cabe toda `P` | `P>0`, `G<5`, `G+P≤5` | `(G+P,0)` | `(3,2)→(5,0)` |
| Se llena `G` | `P>0`, `G<5`, `G+P>5` | `(5,G+P−5)` | `(4,2)→(5,1)` |

🟡 La cantidad total `G+P` se conserva durante un trasvase y disminuye al vaciar. Ese invariante permite revisar cualquier rama manuscrita: un trasvase que transforme `(3,2)` en un estado con 6 litros sería incorrecto. Si la foto quería introducir otras acciones, como llenar desde un grifo, no se distinguen aquí; con lo legible bastan las acciones anteriores para llegar al objetivo.

## Foto 2 · lectura de los apuntes

🟢 El título «Propiedades [sigla de profundización iterativa: **sigla dudosa**]» lista: `1) Completo: sí, siempre encuentra solución`; `2) Admisible: no, en general. Solo encuentra la menos profunda`; `3) Consumo tiempo y memoria`. Dibuja un árbol de factor de ramificación `b` y profundidad `d`, con `b⁰=1`, `b¹=b`, y suma `b⁰+b¹+...+bᵈ=O(bᵈ)`. Se anota que `d+1` es el número de niveles examinados. (claseFaltante2.jpeg)

🟢 La foto pone `b=10`, `d=5` y compara una sola exploración de `1+10+100+1000+10000+100000=111111` nodos con las visitas repetidas de la profundización iterativa. Señala que el **sobrecoste no cambia el orden de complejidad** y propone IDA* «si hay muchos temas de almacenamiento». (claseFaltante2.jpeg)

🟢❓ El total escrito para la exploración repetida, al extremo derecho, está borroso. A partir de la propia suma de niveles se recalcula abajo; no atribuyo al manuscrito una cifra exacta. (claseFaltante2.jpeg)

🟢 El siguiente encabezado es **IDA\*** (*Iterative Deepening A\**). Se describe como optimización de coste mínimo: «nivel de poda (cota de coste)», `f(n)=g(n)+h(n)`, y «forzamos retroceso si `f(n)>poda`». En cada pasada, la siguiente cota es el **mínimo valor de `f` que provocó un retroceso por poda**. Si hay repeticiones dentro del camino, se pueden omitir. (claseFaltante2.jpeg)

🟢 El ejemplo pequeño de la foto comienza con `h(0)=2`, umbral `2`, `g(0)=0` y `f(0)=2`. Se leen ramas con `f=4`, `f=2,5`, `f=5,4` que sobrepasan el umbral; la siguiente cota anotada es `2,5`. Al pie se recuerda que **`f(n)=poda` no causa retroceso**. (claseFaltante2.jpeg)

🟢❓ Algunos valores y caminos del ejemplo —especialmente las etiquetas de nodos en el margen inferior y el coste que antecede a `5,4`— no se leen con seguridad. Sin el grafo de ese ejemplo no puede completarse de modo fiable; solo sirve para ilustrar la regla de cambio de umbral. (claseFaltante2.jpeg)

## Foto 2 · mecanismo paso a paso

🟡 En una pasada de IDA* se recorre en profundidad, manteniendo una **ruta actual** y su coste real `g`. Para cada nodo se calcula `f=g+h`: una cota inferior de cuánto costaría una solución que continuara por ese nodo, si `h` es admisible. Si `f` excede el umbral, se poda esa rama. Por ejemplo, con umbral `10`, un nodo de `g=7`, `h=4` da `f=11` y se poda; uno de `g=7`, `h=3` da `f=10` y se explora. (Derivado de claseFaltante2.jpeg)

🟡 ¿Por qué el nuevo umbral debe ser el **menor** `f` podado? Si las podas de una pasada fueron `11`, `14` y `12`, cualquier umbral entre `10` y `11` repetiría exactamente las mismas expansiones. `11` es el primer valor que permite avanzar. En la foto, esa lógica lleva de `2` a `2,5`. El umbral no se sube con un incremento fijo. (Derivado de claseFaltante2.jpeg)

🟡 La memoria de profundidad iterativa y de IDA* en su versión de retroceso es proporcional a la longitud del camino actual —más la pila de sucesores—, aproximadamente `O(d)` si la ramificación se genera de forma incremental, u `O(bd)` si se almacenan listas de hasta `b` sucesores por nivel. El tiempo puede repetir expansiones en varios umbrales. Para `b=10,d=5`, la cuenta de una pasada hasta el nivel 5 es `111111`; si los niveles 0 a 5 se reinician por completo, el número de visitas es `6·1+5·10+4·100+3·1000+2·10000+1·100000=123456`. Ambas cantidades son `O(10⁵)` en ese ejemplo. (Cálculo añadido a partir de claseFaltante2.jpeg)

🟡 Con **costes positivos**, ramificación finita, `h` admisible y gestión de ciclos dentro del camino, IDA* encuentra una solución de coste mínimo cuando el objetivo se acepta con `f≤umbral`. Si `h` es consistente, `f` no baja a lo largo de una rama; esto ayuda a interpretar las podas. La propiedad de consistencia y la notación `g,h,f` coinciden con A* del PDF, aunque IDA* usa una cota y retroceso en lugar de una cola global de ABIERTOS. (Contraste con Tema 1.pdf pp.12–17)

🟡 Una diferencia útil para el examen: **profundización iterativa** cambia un límite de profundidad (`0,1,2,…`); **IDA\*** cambia un límite de coste estimado (`f`). En el ejercicio de calles, los arcos tienen tiempos muy diferentes, así que la profundidad sola no garantiza la ruta más rápida.

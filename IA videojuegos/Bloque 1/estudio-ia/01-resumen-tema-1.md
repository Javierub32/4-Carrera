# IA para videojuegos · Bloque 1 · Tema 1

Este resumen cubre **las 19 páginas** del PDF, incluidas las páginas que contienen fotografías o solo un título. 🟢 indica contenido del material y cita la página; 🟡 indica una explicación o cálculo añadido para estudiar. Las fotos de clase tienen su transcripción y explicación extensa en [02-clases-faltantes.md](02-clases-faltantes.md). El ejercicio de IDA* está en [03-ejercicio-ida.md](03-ejercicio-ida.md).

## 1. La idea que organiza el tema

🟢 El curso parte de la IA simbólica. La **hipótesis del sistema de símbolos físicos** (Newell y Simon, 1976) sostiene que un sistema capaz de almacenar expresiones simbólicas y operar sobre ellas posee los medios necesarios y suficientes para la inteligencia general. La **hipótesis de la búsqueda heurística** propone resolver problemas buscando selectivamente en un espacio que represente el problema. Los apuntes distinguen aprendizaje (predicción e inducción) y resolución de problemas (deducción). (Tema 1.pdf p.3)

🟡 Imagina un personaje de videojuego que debe llegar a una salida. Antes de elegir un algoritmo, hay que decidir qué cuenta como una situación distinta, qué movimientos son posibles y cuándo se ha ganado. Esa representación determina el grafo sobre el que actuará la búsqueda.

🟢 La pizarra sitúa el tema en entornos de **un solo agente, totalmente observables, deterministas y conocidos**; ahí la solución se describe como un plan. En entornos con adversario aparece un plan condicional o una acción inmediata; con incertidumbre, una política. La foto del PDF contiene las etiquetas adversario/AND-OR y incertidumbre/backgammon como contexto. (Tema 1.pdf p.4)

## 2. Espacios de estados: representar antes de buscar

🟢 Un **espacio de estados** es una representación formal de la parte relevante del mundo. Incluye un conjunto de estados `E` y acciones que permiten pasar de un estado a otro. Una acción tiene **precondición** `E → {verdadero, falso}` y **efecto** `E → E`. Un problema añade estado inicial y condición objetivo; la solución es una secuencia válida de acciones desde el inicial hasta un estado objetivo. (Tema 1.pdf pp.5–6)

🟢 La pizarra usa misioneros y caníbales: estado `(B,M,C)`, donde `B` indica la orilla de la barca y `M,C` cuentan personas en una orilla; estado inicial `(1,3,3)`, objetivo `(0,0,0)`. Figuran las travesías de un misionero, dos misioneros, un caníbal, dos caníbales o uno de cada. El dibujo exige cantidades entre 0 y 3 y que en ninguna orilla con misioneros estos sean superados por caníbales. Cada viaje necesita barca en la orilla de salida, pasajeros disponibles y un estado resultante válido. (Tema 1.pdf pp.5–6)

🟡 Un ejemplo de precondición: «cruzan dos caníbales desde la orilla inicial» solo se aplica si la barca está allí y hay al menos dos caníbales. Tras moverlos se comprueban ambas orillas. La validez del estado resultante es una restricción del problema, no una preferencia del algoritmo.

🟢 En el **puzle de ocho piezas**, el estado es una matriz `3×3`; `0` representa el hueco. El ejemplo inicial es `[[3,4,2],[0,1,5],[7,6,8]]`, con hueco `(fila 1, columna 0)` usando índices desde cero. Mover el hueco arriba, abajo, derecha o izquierda intercambia `0` con la pieza vecina y cuesta `1`; las precondiciones son, respectivamente, `fila>0`, `fila<2`, `columna<2` y `columna>0`. (Tema 1.pdf pp.7–8)

🟢 Para indexar permutaciones, los apuntes definen `d(t₁,⟨t₁,…,tₖ⟩)` como la posición de `t₁` entre los elementos de esa tupla ordenados de menor a mayor, empezando por cero. Entonces `rankp(⟨t₁,…,tₖ⟩)=d(t₁,⟨t₁,…,tₖ⟩)(k−1)!+rankp(⟨t₂,…,tₖ⟩)`, con `rankp(⟨t₁⟩)=0`. Ejemplo: `rankp(⟨2,0,1⟩)=2·2!+0·1!=4`. Los apuntes anotan `unrankp` pero no desarrollan el algoritmo («NO CAE»). (Tema 1.pdf p.8)

🟡 Esta fórmula da un índice entero a cada ordenación sin almacenar una cadena por estado. No hay que confundir `d` con la distancia del camino: aquí es un dígito del código factorial de la permutación.

🟢 Un espacio de estados define implícitamente un grafo: **estados = nodos; acciones = arcos**. La página distingue estrategias irrevocables, como búsqueda local y algoritmos genéticos, que siguen un camino, y estrategias tentativas. Entre estas, la búsqueda con árbol guarda caminos interesantes; la búsqueda con retroceso guarda solo el camino actual. La figura de la página muestra estados de un problema de jarras conectados por acciones, incluido un camino ilustrativo entre estados. (Tema 1.pdf p.9)

## 3. Búsqueda con árbol: amplitud y Dijkstra

🟢 Para definir un método de búsqueda con árbol se concretan tres decisiones: **qué nodo de ABIERTOS seleccionar**, qué hacer si aparecen dos caminos hacia el mismo estado y **cuándo terminar**. Las propiedades que se comparan son completitud, admisibilidad u optimalidad, consumo de memoria y tiempo. «Completo» significa encontrar alguna solución si existe; «admisible» en estos apuntes significa encontrar una solución óptima. (Tema 1.pdf p.10)

🟢 En **búsqueda en amplitud**, los nuevos nodos se ponen al final de una cola. Si aparece otra ruta a un nodo, los apuntes conservan la antigua. Puede terminarse cuando se genera o selecciona el objetivo, según la versión. Es completa con ramificación finita y solución a profundidad finita. No es óptima para costes arbitrarios; sí cuando cada arco cuesta lo mismo. Para factor de ramificación `b` y objetivo a profundidad `d`, el material señala memoria de orden `O(b^d)`. (Tema 1.pdf p.10)

🟡 La cola hace que se prueben antes los caminos de menos pasos, no los de menor tiempo. Si un arco tarda `100 s` y dos arcos tardan `1+1 s`, amplitud prefiere el de un paso.

🟢 **Dijkstra** es búsqueda a ciegas por coste: selecciona en ABIERTOS el menor `g(n)`, conserva la ruta con menor `g` cuando hay duplicados y termina cuando selecciona el objetivo. `g(n)` es el coste del camino almacenado desde la raíz `S` hasta `n`. El coste de un camino es la suma de sus arcos; el texto considera costes no negativos y, para ciertas garantías en grafos infinitos, una cota positiva `ε` por arco. La pizarra resuelve un ejemplo con coste final `43` y ruta `C1→C2→C3→Cφ` según su rotulación. (Tema 1.pdf pp.10–11)

🟡 Terminar al **generar** el objetivo sería peligroso para Dijkstra: podría aparecer después una ruta más barata. La primera selección del objetivo con coste mínimo sí certifica el coste óptimo bajo los supuestos habituales.

## 4. A*: coste recorrido más estimación pendiente

🟢 A* ordena ABIERTOS por `f(n)=g(n)+h(n)`: `g` es el coste del camino conocido hasta `n`; `h` estima el coste de `n` al objetivo; `f` estima el coste de una solución que pase por `n`. Si se encuentra una ruta mejor a un nodo, se cambia su predecesor; si estaba CERRADO, puede reabrirse. A* termina al seleccionar el objetivo. La pizarra de la página 12 muestra un ejemplo con una tabla de sucesores y la ruta final `C4→C7→C6→C3→C1`. (Tema 1.pdf pp.11–13)

🟢 La notación incluye `C*` para el coste óptimo de una solución, `g*(n)` para el coste óptimo real desde `S` hasta `n`, y `h*(n)` para el coste óptimo real desde `n` al objetivo; `g(n)≥g*(n)`. Una heurística **optimista o admisible** cumple `h(n)≤h*(n)` para todo `n`. En las páginas 13 y 15 aparecen los lemas: con las hipótesis de la demostración A* termina, el camino óptimo no se poda y no puede terminar con una solución subóptima. (Tema 1.pdf pp.13,15)

🟡 La razón central es esta: para cualquier nodo `n` del camino óptimo, `g*(n)+h(n)≤g*(n)+h*(n)=C*`. Mientras la solución óptima no se haya encontrado, queda algún nodo de ese camino con una cota no mayor que `C*`. Una solución de coste mayor no puede ser seleccionada antes que todos esos candidatos si la implementación gestiona correctamente los duplicados. En un grafo, admitir una `h` solo optimista puede exigir reabrir estados.

🟢 Los apuntes llaman **más informado** a un heurístico `h₂` que domina a `h₁` (`h₂(n)≥h₁(n)` sin sobreestimar). Afirman que A* con `h₂` expande menos nodos que con `h₁`; ilustran A* frente a Dijkstra con una región elíptica frente a un disco de búsqueda. También advierten que A* puede volver a expandir estados y que eso puede costar tiempo. (Tema 1.pdf pp.13–16)

🟡 La afirmación sobre **todos** los nodos expandidos requiere matizar empates en `f=C*`, criterio de desempate e implementación de grafo. Lo seguro es que una heurística dominadora reduce o mantiene el conjunto de expansiones obligadas con `f<C*`; el número real de expansiones puede diferir en la frontera por esos detalles.

🟢 El ejercicio geométrico del PDF plantea una red densa con `p` nodos por unidad de superficie, origen `A=(0,0)`, objetivo `Z=(0,a)`, solución óptima de coste `C` y heurística euclídea. Pide cotas de nodos para Dijkstra y A*. El PDF muestra un caso `a=10`, `C=12`: disco azul de Dijkstra y elipse roja de A*. **El enunciado no incluye una solución escrita.** (Tema 1.pdf pp.14,16)

🟡 Si el coste mide distancia y se idealiza la red como continua, Dijkstra examina como máximo una región de área `πC²`, aproximadamente `pπC²` nodos. A* necesita `dist(A,n)+dist(n,Z)≤C`, una elipse de semiejes `C/2` y `√(C²−a²)/2`; su área es `πC√(C²−a²)/4`, aproximadamente `p` veces esa área. Son cotas geométricas aproximadas: bordes, discretización, empates y reaperturas pueden alterar el recuento exacto.

🟢 Una heurística es **monótona o consistente** si, para todo arco `n→n'`, `h(n)≤c(n,n')+h(n')`. Si el arco cuesta `1`, queda `h(n)≤1+h(n')`. De ello resulta que `f` no disminuye al avanzar por un camino. En A* de grafo, cuando se selecciona un estado para expansión, `g(n)=g*(n)` y no hace falta reabrirlo; cada estado se expande a lo sumo una vez. (Tema 1.pdf pp.15–17)

🟢 Para movimientos horizontales o verticales de coste unitario, la distancia **Manhattan** al objetivo es `h_M(n)=|x−x*|+|y−y*|`. Un movimiento cambia esa distancia como máximo en `1`, por lo que cumple la desigualdad de consistencia. La página 13 incluye una nota sin resolver que duda entre heurística euclídea y Manhattan; la definición formal de Manhattan está en la página 17. (Tema 1.pdf pp.13,16–17)

🟡 En el ejercicio de calles la distancia Manhattan está en **metros**, mientras `g` mide **segundos**. Por eso se divide por una velocidad máxima admisible; sumar directamente metros y segundos sería un error de unidades. La demostración concreta se desarrolla en [03-ejercicio-ida.md](03-ejercicio-ida.md).

## 5. Retroceso, profundización iterativa e IDA*

🟢 La sección de **búsqueda con marcha atrás** dice que backtracking no es completo en general, aunque sí lo es en árboles finitos o grafos acíclicos dirigidos finitos; tampoco es admisible en general. El apartado «Consumo tiempo / memoria» aparece como título sin cifras en el PDF. (Tema 1.pdf p.18)

🟢 Las dos fotos de clase añaden backtracking acotado, búsqueda en profundidad iterativa y **IDA\***. En profundidad iterativa se repite una búsqueda con límite de profundidad creciente; IDA* utiliza `f=g+h` como límite de **coste estimado** y, tras cada pasada fallida, toma el menor `f` que sobrepasó el límite. La explicación, transcripción de las fórmulas de las jarras y dudas de lectura están en [02-clases-faltantes.md](02-clases-faltantes.md). (claseFaltante1.jpeg; claseFaltante2.jpeg)

🟢 La página 19 contiene únicamente el título «Heurísticos Bases de Datos de Patrones» y la fecha. **No hay definiciones, algoritmo ni ejemplo de bases de datos de patrones en este PDF.** (Tema 1.pdf p.19)

## Tabla de cobertura de fuentes

| Fuente | Cobertura inspeccionada | Qué aporta |
| --- | --- | --- |
| Tema 1.pdf | pp. **1–3** | portada, índice e IA simbólica |
| Tema 1.pdf | pp. **4–6** | foto de pizarra de entornos; espacio de estados; misioneros y caníbales; estado inicial, objetivo y plan |
| Tema 1.pdf | pp. **7–9** | puzle 8, acciones, `rankp`, grafo implícito, tipos de búsqueda; figura de jarras |
| Tema 1.pdf | pp. **10–12** | amplitud, Dijkstra, ejemplo de coste 43, A* y ejemplo de pizarra |
| Tema 1.pdf | pp. **13–15** | notación de óptimos, admisibilidad, lemas, heurísticas informadas, ejercicio geométrico |
| Tema 1.pdf | pp. **16–17** | figura disco/elipse, consistencia, Manhattan |
| Tema 1.pdf | pp. **18–19** | propiedades de backtracking; p.19 es solo encabezado |
| claseFaltante1.jpeg | foto completa | backtracking acotado, profundidad iterativa, jarras de 5 l y 2 l |
| claseFaltante2.jpeg | foto completa | propiedades y coste de profundidad iterativa; reglas y ejemplo de IDA* |
| enunciadoIDA.png | imagen completa | red dirigida, unidades, velocidades, estado inicial y objetivo |
| ejercicioIDA1.jpeg → ejercicioIDA2.jpeg → ejercicioIDA3.jpeg | tres fotos completas, **en ese orden** | umbrales y cálculos del borrador; desarrollo parcial del árbol; definición del heurístico |

🟡 La extracción automática de texto del PDF omitía contenido de las páginas **4 y 6**, porque son fotografías. Las inspeccioné visualmente; también revisé la figura de jarras (p.9), las pizarras de Dijkstra y A* (pp.11–12), la foto de propiedades (p.15), el gráfico (p.16) y la página 19 para confirmar que solo tiene título.

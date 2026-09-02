Integrantes

Joaquín Adra
CU: Dado un festival, listar todos sus pedidos con los detalles de cada uno, distinguiendo si la unidad de venta es un FoodTruck o un PuestoDesarmable.

Nacho Puig
CU: Listar el staff de una unidad de venta filtrando por rol (Cajero / Cocinero). Demuestra herencia (Personal → Cajero, Cocinero) y relación uno-a-muchos (UnidadVenta → staff).

Leandro Kaul
CU: Calcular el canon que debe pagar cada unidad de venta de un festival según su tipo. Demuestra herencia (UnidadVenta → FoodTruck, PuestoDesarmable) con calcularCannon() polimórfico (FoodTruck suma plus de electricidad si aplica, PuestoDesarmable suma costo de montaje) y relación uno-a-muchos (Festival → unidades).

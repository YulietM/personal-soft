SELECT DISTINCT c.nombre
FROM BTG.Cliente c
         JOIN BTG.Inscripcion i ON c.id = i.idCliente
         JOIN BTG.Disponibilidad d ON i.idProducto = d.idProducto
         JOIN BTG.Visitan v ON d.idSucursal = v.idSucursal AND v.idCliente = c.id;


--version mejorada, asi evitamos que personas con el mismo nombre queden por fuera
SELECT DISTINCT c.id, c.nombre
FROM BTG.Cliente c
         JOIN BTG.Inscripcion i ON c.id = i.idCliente
         JOIN BTG.Disponibilidad d ON i.idProducto = d.idProducto
         JOIN BTG.Visitan v ON d.idSucursal = v.idSucursal AND v.idCliente = c.id;
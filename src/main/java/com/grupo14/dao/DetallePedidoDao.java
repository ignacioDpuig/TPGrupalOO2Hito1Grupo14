package com.grupo14.dao;

import com.grupo14.datos.DetallePedido;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetallePedidoDao extends BaseDao {

    public void agregar(DetallePedido objeto) {
        try {
            iniciaOperacion();
            session.save(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public void actualizar(DetallePedido objeto) {
        try {
            iniciaOperacion();
            session.merge(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public void eliminar(DetallePedido objeto) {
        try {
            iniciaOperacion();
            objeto = (DetallePedido) session.merge(objeto);
            session.delete(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public Set<DetallePedido> traerPorPedido(int idPedido) {
        Set<DetallePedido> detallesDelPedido = new HashSet<>();
        try {
            iniciaOperacion();
            Query<DetallePedido> query = session.createQuery(
                    "from DetallePedido d where d.pedido.id = :idPedido", DetallePedido.class
            );
            query.setParameter("idPedido", idPedido);
            detallesDelPedido = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return detallesDelPedido;
    }

}
package com.grupo14.dao;

import com.grupo14.datos.Pedido;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class PedidoDao extends BaseDao{

    public int agregar(Pedido objeto) {
        int id = 0;
        try {
            iniciaOperacion();
            id = Integer.parseInt(session.save(objeto).toString());
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
        return id;
    }

    public void actualizar(Pedido objeto) {
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

    public void eliminar(Pedido objeto) {
        try {
            iniciaOperacion();
            objeto = (Pedido) session.merge(objeto);
            session.delete(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public Pedido traer(int id) {
        Pedido objeto = null;
        try {
            iniciaOperacion();
            objeto = session.get(Pedido.class, id);
        } finally {
            session.close();
        }
        return objeto;
    }

    public Set<Pedido> traer() {
        Set<Pedido> pedidos = new HashSet<>();
        try {
            iniciaOperacion();
            Query<Pedido> query = session.createQuery(
                    "from Pedido p order by p.fecha desc", Pedido.class
            );
            pedidos = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return pedidos;
    }

    public Set<Pedido> traerPorFecha(LocalDate fecha) {
        Set<Pedido> pedidosPorFecha = new HashSet<>();
        try {
            iniciaOperacion();
            Query<Pedido> query = session.createQuery(
                    "from Pedido p where p.fecha = :fecha order by p.fecha desc", Pedido.class
            );
            query.setParameter("fecha", fecha);
            pedidosPorFecha = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return pedidosPorFecha;
    }

    public Set<Pedido> traerPorFestival(int idFestival) {
        Set<Pedido> pedidosPorFestival = new HashSet<>();
        try {
            iniciaOperacion();
            Query<Pedido> query = session.createQuery(
                    "from Pedido p where p.festival.id = :idFestival order by p.fecha desc", Pedido.class
            );
            query.setParameter("idFestival", idFestival);
            pedidosPorFestival = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return pedidosPorFestival;
    }

    public Set<Pedido> traerPorUnidad(int idUnidad) {
        Set<Pedido> pedidosPorUnidad = new HashSet<>();
        try {
            iniciaOperacion();
            Query<Pedido> query = session.createQuery(
                    "from Pedido p where p.unidad.id = :idUnidad order by p.fecha desc", Pedido.class
            );
            query.setParameter("idUnidad", idUnidad);
            pedidosPorUnidad = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return pedidosPorUnidad;
    }
}
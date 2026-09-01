package com.grupo14.dao;

import com.grupo14.datos.PuestoDesarmable;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class PuestoDesarmableDao extends BaseDao {

    public int agregar(PuestoDesarmable objeto) {
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

    public void actualizar(PuestoDesarmable objeto) {
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

    public void eliminar(PuestoDesarmable objeto) {
        try {
            iniciaOperacion();
            objeto = (PuestoDesarmable) session.merge(objeto);
            session.delete(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public PuestoDesarmable traer(int id) {
        PuestoDesarmable objeto = null;
        try {
            iniciaOperacion();
            objeto = session.get(PuestoDesarmable.class, id);
        } finally {
            session.close();
        }
        return objeto;
    }

    public Set<PuestoDesarmable> traer() {
        Set<PuestoDesarmable> puestosDesarmables = new HashSet<>();
        try {
            iniciaOperacion();
            Query<PuestoDesarmable> query = session.createQuery(
                    "from PuestoDesarmable pd order by pd.nombreComercial asc", PuestoDesarmable.class
            );
            puestosDesarmables = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return puestosDesarmables;
    }

    public Set<PuestoDesarmable> traerPorCantidadCarpas(int cantidadCarpas) {
        Set<PuestoDesarmable> puestosDesarmables = new HashSet<>();
        try {
            iniciaOperacion();
            Query<PuestoDesarmable> query = session.createQuery(
                    "from PuestoDesarmable pd where pd.cantidadCarpas = :cantidad", PuestoDesarmable.class
            );
            query.setParameter("cantidad", cantidadCarpas);
            puestosDesarmables = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return puestosDesarmables;
    }
}
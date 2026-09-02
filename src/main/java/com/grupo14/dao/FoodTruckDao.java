package com.grupo14.dao;

import com.grupo14.datos.FoodTruck;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodTruckDao extends BaseDao {
    public int agregar(FoodTruck objeto) {
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

    public void actualizar(FoodTruck objeto) {
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

    public void eliminar(FoodTruck objeto) {
        try {
            iniciaOperacion();
            objeto = (FoodTruck) session.merge(objeto);
            session.delete(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public FoodTruck traer(int id) {
        FoodTruck objeto = null;
        try {
            iniciaOperacion();
            objeto = session.get(FoodTruck.class, id);
        } finally {
            session.close();
        }
        return objeto;
    }

    public FoodTruck traerPorPatente(String patente) {
        FoodTruck foodTruck = null;
        try {
            iniciaOperacion();
            foodTruck = (FoodTruck) session.createQuery(
                    "from FoodTruck ft where ft.patente = :patente"
            ).setParameter("patente", patente).uniqueResult();
        } finally {
            session.close();
        }
        return foodTruck;
    }

    public Set<FoodTruck> traer() {
        Set<FoodTruck> foodTrucks = new HashSet<>();
        try {
            iniciaOperacion();
            Query<FoodTruck> query = session.createQuery(
                    "from FoodTruck ft order by ft.nombreComercial asc", FoodTruck.class
            );
            foodTrucks = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return foodTrucks;
    }

    public Set<FoodTruck> traerConConexion() {
        Set<FoodTruck> foodTrucksConConexion = new HashSet<>();
        try {
            iniciaOperacion();
            Query<FoodTruck> query = session.createQuery(
                    "from FoodTruck ft where ft.requiereConexion = true", FoodTruck.class
            );
            foodTrucksConConexion = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return foodTrucksConConexion;
    }
}
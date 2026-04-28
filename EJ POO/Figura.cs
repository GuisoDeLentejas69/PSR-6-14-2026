using System;

namespace ProyectoSuperheroes
{
    public class Figura
    {
        private string codigo;
        private double precio;
        private Superheroe superheroe;
        private Dimension dimension;

        public Figura(string codigo, double precio, Dimension dimension, Superheroe superheroe)
        {
            this.codigo = codigo;
            this.precio = precio;
            this.dimension = dimension;
            this.superheroe = superheroe;
        }

        public string Codigo
        {
            get { return codigo; }
            set { codigo = value; }
        }

        public double Precio
        {
            get { return precio; }
            set { precio = value; }
        }

        public Superheroe Superheroe
        {
            get { return superheroe; }
            set { superheroe = value; }
        }

        public Dimension Dimension
        {
            get { return dimension; }
            set { dimension = value; }
        }

        public void subirPrecio(double cantidad)
        {
            precio += cantidad;
        }

        public override string ToString()
        {
            return "Código: " + codigo +
                   " | Precio: " + precio +
                   "\n" + superheroe.ToString() +
                   "\n" + dimension.ToString();
        }
    }
}
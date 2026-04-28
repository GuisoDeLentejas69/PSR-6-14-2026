using System;
using System.Collections;

namespace ProyectoSuperheroes
{
    public class Coleccion
    {
        private string nombreColeccion;
        private ArrayList listaFiguras;

        public Coleccion(string nombre)
        {
            nombreColeccion = nombre;
            listaFiguras = new ArrayList();
        }

        public string NombreColeccion
        {
            get { return nombreColeccion; }
            set { nombreColeccion = value; }
        }

        public void añadirFigura(Figura fig)
        {
            listaFiguras.Add(fig);
        }

        public void subirPrecio(double cantidad, string id)
        {
            foreach (Figura f in listaFiguras)
            {
                if (f.Codigo == id)
                {
                    f.subirPrecio(cantidad);
                }
            }
        }

        public string conCapa()
        {
            string resultado = "";

            foreach (Figura f in listaFiguras)
            {
                if (f.Superheroe.Capa)
                {
                    resultado += f.ToString() + "\n";
                }
            }

            return resultado;
        }

        public Figura masValioso()
        {
            Figura mayor = null;

            foreach (Figura f in listaFiguras)
            {
                if (mayor == null || f.Precio > mayor.Precio)
                {
                    mayor = f;
                }
            }

            return mayor;
        }

        public double getValorColeccion()
        {
            double total = 0;

            foreach (Figura f in listaFiguras)
            {
                total += f.Precio;
            }

            return total;
        }

        public double getVolumenColeccion()
        {
            double volumen = 0;

            foreach (Figura f in listaFiguras)
            {
                volumen += f.Dimension.getVolumen();
            }

            return volumen + 200;
        }

        public override string ToString()
        {
            string resultado = "Colección: " + nombreColeccion + "\n";

            foreach (Figura f in listaFiguras)
            {
                resultado += f.ToString() + "\n";
            }

            return resultado;
        }
    }
}
using System;

namespace ProyectoSuperheroes
{
    public class Superheroe
    {
        private string nombre;
        private string descripcion;
        private bool capa;

        public Superheroe(string nombre)
        {
            this.nombre = nombre;
            this.descripcion = "";
            this.capa = false;
        }

        public string Nombre
        {
            get { return nombre; }
            set { nombre = value; }
        }

        public string Descripcion
        {
            get { return descripcion; }
            set { descripcion = value; }
        }

        public bool Capa
        {
            get { return capa; }
            set { capa = value; }
        }

        public override string ToString()
        {
            return "Nombre: " + nombre +
                   " | Descripción: " + descripcion +
                   " | Capa: " + capa;
        }
    }
}
using System;

namespace ProyectoSuperheroes
{
    public class Dimension
    {
        private double alto;
        private double ancho;
        private double profundidad;

        public Dimension()
        {
            alto = 0;
            ancho = 0;
            profundidad = 0;
        }

        public Dimension(double alto, double ancho, double profundidad)
        {
            this.alto = alto;
            this.ancho = ancho;
            this.profundidad = profundidad;
        }

        public double Alto
        {
            get { return alto; }
            set { alto = value; }
        }

        public double Ancho
        {
            get { return ancho; }
            set { ancho = value; }
        }

        public double Profundidad
        {
            get { return profundidad; }
            set { profundidad = value; }
        }

        public double getVolumen()
        {
            return alto * ancho * profundidad;
        }

        public override string ToString()
        {
            return "Alto: " + alto +
                   " | Ancho: " + ancho +
                   " | Profundidad: " + profundidad +
                   " | Volumen: " + getVolumen();
        }
    }
}
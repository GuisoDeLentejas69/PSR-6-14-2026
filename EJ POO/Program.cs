using System;

namespace ProyectoSuperheroes
{
    class Program
    {
        static void Main(string[] args)
        {
            Superheroe batman = new Superheroe("Batman");
            batman.Descripcion = "Traje negro";
            batman.Capa = true;

            Superheroe spiderman = new Superheroe("Spiderman");
            spiderman.Descripcion = "Rojo y azul";
            spiderman.Capa = false;

            Dimension d1 = new Dimension(10, 5, 3);
            Dimension d2 = new Dimension(8, 4, 2);

            Figura f1 = new Figura("BAT01", 1500, d1, batman);
            Figura f2 = new Figura("SPI01", 1200, d2, spiderman);

            Coleccion coleccion = new Coleccion("SuperHeroes");

            coleccion.añadirFigura(f1);
            coleccion.añadirFigura(f2);

            Console.WriteLine(coleccion.ToString());
            Console.WriteLine("Figuras con capa:");
            Console.WriteLine(coleccion.conCapa());
            Console.WriteLine("Más valioso: " + coleccion.masValioso());
            Console.WriteLine("Valor total: " + coleccion.getValorColeccion());
            Console.WriteLine("Volumen total: " + coleccion.getVolumenColeccion());
        }
    }
}
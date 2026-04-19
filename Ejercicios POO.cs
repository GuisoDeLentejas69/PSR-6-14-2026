using System;
using System.Collections;

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
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class HelloLauncher
{
    public static void main (String[] args)
    {
    HelloWorldImage myProgram = new HelloWorldImage();
    LwjglApplication launcher = new LwjglApplication( myProgram );
     }
}
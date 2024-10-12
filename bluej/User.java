
/**
 * Datenstruktur Klasse User
 * 
 * @author LurkingBytes
 * @version 02.10.2024
 */
public class User
{
    private String name;
    private String ip;
    private int port;

    public User(String pName, String pIP, int pPort)
    {
        name = pName;
        ip = pIP;
        port = pPort;
    }
    
    private String gibHost()
    {
        return ip + ":" + port;
    }

    public String gibName()
    {
        return name;
    }

    public String gibIP()
    {
        return ip;
    }
    
    public int gibPort()
    {
        return port;
    }
}

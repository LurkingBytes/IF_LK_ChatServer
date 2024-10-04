
/**
 * Datenstruktur Klasse User
 * 
 * @author LurkingBytes
 * @version 02.10.2024
 */
public class User
{
    protected String name;
    protected String ip;

    public User(String pName, String pIP)
    {
        name = pName;
        ip = pIP;
    }

    public String gibName()
    {
        return name;
    }

    public String gibIP()
    {
        return ip;
    }
}

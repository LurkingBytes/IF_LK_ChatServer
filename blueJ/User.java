
/**
 * Datenstruktur Klasse User
 * 
 * @author LurkingBytes
 * @version 02.10.2024
 */
public class User implements ComparableContent<User>
{
    private String name;
    private String ip;

    public User(String pName, String pIP)
    {
        name = pName;
        ip = pIP;
    }

    public boolean isGreater(User pContent)
    {
        return ip.compareTo(pContent.gibIP()) < 0;
    }

    public boolean isEqual(User pContent)
    {
        return ip.compareTo(pContent.gibIP()) == 0;
    }

    public boolean isLess(User pContent)
    {
        return ip.compareTo(pContent.gibIP()) > 0;
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


/**
 * Datenstruktur Klasse UserIP
 * 
 * @author LurkingBytes
 * @version 02.10.2024
 */
public class UserIP extends User implements ComparableContent<UserIP>
{
    public UserIP(String pName, String pIP)
    {
        super(pName, pIP);
    }

    public boolean isGreater(UserIP pContent)
    {
        return ip.compareTo(pContent.gibIP()) < 0;
    }

    public boolean isEqual(UserIP pContent)
    {
        return ip.compareTo(pContent.gibIP()) == 0;
    }

    public boolean isLess(UserIP pContent)
    {
        return ip.compareTo(pContent.gibIP()) > 0;
    }
}

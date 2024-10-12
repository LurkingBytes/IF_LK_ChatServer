
/**
 * Datenstruktur Klasse UserName
 * 
 * @author LurkingBytes
 * @version 04.10.2024
 */
public class UserName extends User implements ComparableContent<UserName>
{
    public UserName(String pName, String pIP, int pPort)
    {
        super(pName, pIP, pPort);
    }

    public boolean isGreater(UserName pContent)
    {
        return name.compareTo(pContent.gibName()) > 0;
    }

    public boolean isEqual(UserName pContent)
    {
        return name.compareTo(pContent.gibName()) == 0;
    }

    public boolean isLess(UserName pContent)
    {
        return name.compareTo(pContent.gibName()) < 0;
    }
}

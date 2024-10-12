
/**
 * Datenstruktur Klasse UserIP
 * 
 * @author LurkingBytes
 * @version 02.10.2024
 */
public class UserIP extends User implements ComparableContent<UserIP>
{
    public UserIP(String pName, String pIP, int pPort)
    {
        super(pName, pIP, pPort);
    }

    public boolean isGreater(UserIP pContent)
    {
        return gibHost().compareTo(pContent.gibHost()) > 0;
    }

    public boolean isEqual(UserIP pContent)
    {
        return gibHost().compareTo(pContent.gibHost()) == 0;
    }

    public boolean isLess(UserIP pContent)
    {
        return gibHost().compareTo(pContent.gibHost()) < 0;
    }
}

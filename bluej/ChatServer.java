
/**
 * Controller Klasse ChatServer
 * 
 * @author LurkingBytes
 * @version 02.10.2024
 */
public class ChatServer extends Server
{
    private BinarySearchTree<UserIP> ips = new BinarySearchTree<>();
    private BinarySearchTree<UserName> namen = new BinarySearchTree<>();

    public ChatServer()
    {
        super(12346);
    }

    public void processNewConnection(String pClientIP, int pClientPort)
    {
        send(pClientIP, pClientPort, "+OK Server ready");
    }

    public void processMessage(String pClientIP, int pClientPort, String pMessage)
    {
        String[] splitMessages = pMessage.split(" ");
        if (splitMessages.length >= 2)
        {
            if (splitMessages[0].equalsIgnoreCase("USER"))
            {
                User user = new User(splitMessages[1], pClientIP, pClientPort);
                User name = namen.search(gibUserName(user));
                if (name == null)
                {
                    ips.insert(gibUserIP(user));
                    namen.insert(gibUserName(user));
                    send(pClientIP, pClientPort, "+OK User " + user.gibName() + " logged in");
                    broadcastAlle("ADDED " + user.gibName());
                }
                else
                {
                    send(pClientIP, pClientPort, "-ERR this username has already been taken");
                }
            }
            else if (splitMessages[0].equalsIgnoreCase("SEND"))
            {
                String nachricht = "";
                for (int i = 1; i < splitMessages.length; i++)
                {
                    nachricht += " " + splitMessages[i];
                }
                User user = ips.search(new UserIP("", pClientIP, pClientPort));
                if (user != null)
                {
                    if (!nachricht.isBlank())
                    {
                        broadcastAlle("MESSAGE " + user.gibName() + nachricht);
                        send(pClientIP, pClientPort, "+OK message has been send");
                    }
                    else
                    {
                        send(pClientIP, pClientPort, "-ERR this message could not be sent");
                    }
                }
                else
                {
                    send(pClientIP, pClientPort, "-ERR Not logged in");
                }
            }
            else
            {
                send(pClientIP, pClientPort, "-ERR this command does not exist");
            }
        }
        else if (splitMessages.length >= 1)
        {
            if (splitMessages[0].equalsIgnoreCase("USERLIST"))
            {
                send(pClientIP, pClientPort, "+OK Userlist");
                List<User> userlist = new List<>();
                fuelleListe(userlist, namen);
                userlist.toFirst();
                while (userlist.hasAccess())
                {
                    send(pClientIP, pClientPort, userlist.getContent().gibName());
                    userlist.next();
                }
                send(pClientIP, pClientPort, ".");
            }
            else if (splitMessages[0].equalsIgnoreCase("QUIT"))
            {
                send(pClientIP, pClientPort, "+OK bye");
                User user = ips.search(new UserIP("", pClientIP, pClientPort));
                if (user != null)
                {
                    ips.remove(gibUserIP(user));
                    namen.remove(gibUserName(user));
                    broadcastAlle("QUIT " + user.gibName());
                }
            }
            else
            {
                send(pClientIP, pClientPort, "-ERR this command does not exist");
            }
        }
        else
        {
            send(pClientIP, pClientPort, "-ERR this command does not exist");
        }
    }

    public void processClosingConnection(String pClientIP, int pClientPort)
    {
        User user = ips.search(new UserIP("", pClientIP, pClientPort));
        if (user != null)
        {
            ips.remove(gibUserIP(user));
            namen.remove(gibUserName(user));
            broadcastAlle("QUIT " + user.gibName());
        }
    }

    public void broadcastAlle(String pMessage)
    {
        sendToAll(pMessage);
    }

    public void fuelleListe(List<User> pListe, BinarySearchTree<UserName> pBaum)
    {
        if (pBaum == null)
        {
            return;
        }
        fuelleListe(pListe, pBaum.getLeftTree());
        if (!pBaum.isEmpty())
        {
            pListe.append(pBaum.getContent());
        }
        fuelleListe(pListe, pBaum.getRightTree());
    }

    public UserIP gibUserIP(User pUser)
    {
        return new UserIP(pUser.gibName(), pUser.gibIP(), pUser.gibPort());
    }

    public UserName gibUserName(User pUser)
    {
        return new UserName(pUser.gibName(), pUser.gibIP(), pUser.gibPort());
    }
}

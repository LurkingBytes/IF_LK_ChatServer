
/**
 * Controller Klasse ChatServer
 * 
 * @author LurkingBytes
 * @version 02.10.2024
 */
public class ChatServer extends Server
{
    private BinarySearchTree<User> ips = new BinarySearchTree<>();
    private BinarySearchTree<User> namen = new BinarySearchTree<>();

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
                User user = new User(splitMessages[1], pClientIP);
                User name = namen.search(user);
                if (name == null)
                {
                    ips.insert(user);
                    namen.insert(user);
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
                User user = ips.search(new User("", pClientIP));
                if (user != null)
                {
                    if (!nachricht.isBlank())
                    {
                        broadcastAlle("MESSAGE " + user.gibName() + nachricht);
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
                User user = ips.search(new User("", pClientIP));
                if (user != null)
                {
                    ips.remove(user);
                    namen.remove(user);
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
        User user = ips.search(new User("", pClientIP));
        if (user != null)
        {
            ips.remove(user);
            namen.remove(user);
            broadcastAlle("QUIT " + user.gibName());
        }
    }

    public void broadcastAlle(String pMessage)
    {
        sendToAll(pMessage);
    }

    public void fuelleListe(List<User> pListe, BinarySearchTree<User> pBaum)
    {
        if (pBaum == null)
        {
            return;
        }
        if (!pBaum.isEmpty())
        {
            pListe.append(pBaum.getContent());
        }
        fuelleListe(pListe, pBaum.getLeftTree());
        fuelleListe(pListe, pBaum.getRightTree());
    }
}

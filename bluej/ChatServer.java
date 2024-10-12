
/**
 * Controller Klasse ChatServer
 * 
 * @author LurkingBytes
 * @version 02.10.2024
 */
public class ChatServer extends Server
{
    private List<User> users = new List<>();

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
                boolean verfuegbar = true;
                users.toFirst();
                while (users.hasAccess())
                {
                    if (users.getContent().gibName().equalsIgnoreCase(user.gibName()))
                    {
                        verfuegbar = false;
                    }
                    users.next();
                }
                if (verfuegbar)
                {
                    users.append(user);
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
                User user = gibUser(pClientIP, pClientPort);
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
                users.toFirst();
                while (users.hasAccess())
                {
                    send(pClientIP, pClientPort, users.getContent().gibName());
                    users.next();
                }
                send(pClientIP, pClientPort, ".");
            }
            else if (splitMessages[0].equalsIgnoreCase("QUIT"))
            {
                send(pClientIP, pClientPort, "+OK bye");
                users.toFirst();
                while (users.hasAccess())
                {
                    User user = users.getContent();
                    if (user.gibIP().equalsIgnoreCase(pClientIP) && user.gibPort() == pClientPort)
                    {
                        users.remove();
                        broadcastAlle("QUIT " + user.gibName());
                    }
                    users.next();
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
        users.toFirst();
        while (users.hasAccess())
        {
            User user = users.getContent();
            if (user.gibIP().equalsIgnoreCase(pClientIP) && user.gibPort() == pClientPort)
            {
                users.remove();
                broadcastAlle("QUIT " + user.gibName());
            }
            users.next();
        }
    }

    public void broadcastAlle(String pMessage)
    {
        sendToAll(pMessage);
    }

    public User gibUser(String pClientIP, int pClientPort)
    {
        users.toFirst();
        while (users.hasAccess())
        {
            User user = users.getContent();
            if (user.gibIP().equalsIgnoreCase(pClientIP) && user.gibPort() == pClientPort)
            {
                return user;
            }
            users.next();
        }
        return null;
    }
}

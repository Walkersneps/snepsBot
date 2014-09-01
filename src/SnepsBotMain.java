import org.jibble.pircbot.*;

public class SnepsBotMain {
    
    public static void main(String[] args) throws Exception {
        
        // Now start our bot up.
        SnepsBot bot = new SnepsBot();
        
        // Enable debugging output.
        bot.setVerbose(true);
        
        // Connect to the IRC server.
        bot.connect("irc.netfuze.net");

        // Join channels.
        bot.joinChannel("#TechSupport");
        bot.joinChannel("#SnepsBot");
        bot.joinChannel("#YNCentral");
        bot.joinChannel("#SuperWalkers");

        //identify
        bot.identify("empoleon");

        //Says he has come
        //bot.sendMessage("#yncentral", "Hello everyone, I'm SnepsBot v.0.8. To know more about me, use the command ',info'");
        //bot.sendMessage("#TechSupport", "Hello everyone, I'm SnepsBot v.0.8. To know more about me, use the command ',info'");


    }
    
}
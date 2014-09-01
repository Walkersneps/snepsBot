import org.jibble.pircbot.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SnepsBot extends PircBot {

    public SnepsBot() {
        this.setName("SnepsBot");
    }


    public Boolean canSpam = false;
    public Boolean canStalk = false;
    public String toStalk1;
    public String toStalk2;
    public String toStalk3;
    public String stalkSentence;
    public Boolean figletMuted = false;
    public static ArrayList<String> stalkingPeople = new ArrayList<>(3);


    public void stalkExtraction (){
        Random randomer = new Random();
        int randomizedNumber = randomer.nextInt((6) + 1) + 1;
        if (randomizedNumber == 1){
            stalkSentence = "Bla Bla Bla";
        } else if (randomizedNumber == 2){
            stalkSentence = "Happy Christmas!";
        } else if (randomizedNumber == 3){
            stalkSentence = "Fag, fAg, faG, FAg, FaG, fAG, FAAAAAAAAG";
        } else if (randomizedNumber == 4){
            stalkSentence = "Yeah, you're right: SuperWalkers is fucking cool!";
        } else if (randomizedNumber == 5){
            stalkSentence = "On the chair the goat sings, under the chair the goat cries";
        }

    }

    private String executeCommand(String command, String out) {

        StringBuffer output = new StringBuffer();

        Process p;
        try {
            //p = Runtime.getRuntime().exec(command);
            p = Runtime.getRuntime().exec(new String[]{"bash","-c",command});
            p.waitFor();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                sendMessage(out, line);
                //output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();

    }

    public void doFigletcooldown(){
        figletMuted = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                figletMuted = false;
            }
        }, 15000); // 3 second wait
    }


    public String currChannel = "#lalala";


    public void onMessage(String channel, String sender,String login, String hostname, String message) {


        //info
        if(message.equalsIgnoreCase(",info")){
            sendMessage(channel, "I'm SnepsBot 0.8, the IRC bot developed by SuperWalkers.");
            sendMessage(channel, "I can do a lot of things, some are funny and mostly useless, other are really useful; if u want to know all of them, just type ',commands'. To see my changelog use ',changelog' or ',oldChanges'.");
        }

        //stalk
        String senderup =  sender.toUpperCase();
        if(canSpam) {
            if (stalkingPeople.contains(senderup)) {
                stalkExtraction();
                sendMessage(channel, stalkSentence);
            }
        }

        //leave channel
        if (message.contains(",bye")) {
            if (sender.equalsIgnoreCase("superwalkers")) {
                sendMessage(channel, "Bye, my Master");
                sendMessage(channel, "Goodbye Everyone :-)");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                partChannel(channel);
            }
            if (!sender.equalsIgnoreCase("superwalkers")) {
                sendMessage(channel, "Bye, " + sender + " :D");
            }
        }
        //disconnect from server
        if (message.contains(",goaway")) {
            if (sender.equalsIgnoreCase("SuperWalkers")) {
                sendMessage(channel, "Adiós, my Master");
                sendMessage(channel, "Goodbye Everyone :-)");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                disconnect();
            }
            if (!sender.equalsIgnoreCase("superwalkers")) {
                sendMessage(channel, "Fuck you, bitch! You can't command me!");
            }
        }

        //bad disconnect
        if (message.contains(",fuckyoubitch")) {
            if (sender.equalsIgnoreCase("superwalkers")) {
                sendMessage(channel, "Fuck you then! >:(");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                disconnect();
            }
            if (!sender.equalsIgnoreCase("superwalkers")) {
                sendMessage(channel, "You are a BITCH!!!");
            }
        }

        //stalk mode on\off
        if (message.startsWith("!!stalk")) {
            if (sender.equalsIgnoreCase("superwalkers")) {
                String mode = message.split(" ")[1];
                if (mode.equalsIgnoreCase("on")) {
                    canStalk = true;
                    sendMessage(channel, "Stalk functions are now activated");
                } else if (mode.equalsIgnoreCase("off")) {
                    canStalk = false;
                    sendMessage(channel, "Stalk functions have been deactivated.");
                } else {
                    sendMessage(channel, "AARARARGGHHHHGA ERRORRRRR BOOOOMM KATPUSH!!!!");
                }
            }
        }

        //random thingy (I love you)
        if (message.contains("I love you")) {
            if (sender.equalsIgnoreCase("SuperWalkers")) {
                sendMessage(channel, "I love you too, Walter");
            }
            else {
                sendMessage(channel, "You are so gay, " + sender);
            }
        }

        //figlet
        if (message.startsWith(",figlet")) {
            if (!figletMuted) {
                currChannel = channel;
                String dump = null;
                String executing = message.substring(("\\figlet ").length());
                if(executing.length() >= 20){
                    sendMessage(channel, "Abort: Message too big.");
                    return;
                }
                String cmd = "figlet " + executing;
                executeCommand(cmd, channel);
                doFigletcooldown();
            }else{
                sendMessage(channel, "This command was recently performed! Please wait!");
            }
        }

        //join channel
        if (message.startsWith(",join")) {
            String channelToJoin = message.split(" ")[1];
            sendMessage(channel, "I'm now joining the channel " + channelToJoin);
            joinChannel(channelToJoin);
            sendMessage(channelToJoin, "Hello Everyone :-)");
        }

        //change nick
        if (message.startsWith(",nick")) {
            if (sender.equalsIgnoreCase("superwalkers")) {
                String newNick = message.split(" ")[1];
                sendMessage(channel, "You don't really like me?!");
                sendAction(channel, "cries");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendRawLine("NICK " + newNick);
            }
            if (!sender.equalsIgnoreCase("superwalkers")) {
                sendMessage(channel, "You can't change me!");
            }
        }

        //reset nickname
        if (message.equalsIgnoreCase(",resetnick")) {
            sendRawLine("NICK SnepsBot");
        }

        //test
        if (message.contains(",test")) {
            sendMessage(channel, "Hello! :-)");
        }

        //ping - pong
        if (message.equalsIgnoreCase(",ping")) {
            sendMessage(channel, "Pong :D");
        }

        //pong - ping
        if (message.equalsIgnoreCase(",pong")) {
            sendMessage(channel, "Ping :D");
        }

        //op
        if (message.startsWith(",op")) {
            if (sender.equalsIgnoreCase("superwalkers")) {
                String toOp = message.split(" ")[1];
                sendRawLine(" OP " + channel + " " + toOp);
            }
        }

        //kick
        if (message.startsWith(",kick")) {
            if (sender.equalsIgnoreCase("superwalkers")) {
                String toKick = message.split(" ")[1];
                sendMessage(channel, "Kicking " + toKick + "...");
                sendRawLine("KICK " + channel + " " + toKick);
            }
        }

        //deop
        if (message.startsWith(",deop")) {
            if (sender.equalsIgnoreCase("superwalkers")) {
                String toDeop = message.split(" ")[1];
                sendRawLine("DEOP " + channel + " " + toDeop);
            }
        }

        //channel spam
        if (message.startsWith(",chanspam")) {
            if (canSpam) {
                String timesToSpam = message.split(" ")[1];
                String textToSpam = message.split(" ")[2];
                int Amount;
                Amount = Integer.parseInt(timesToSpam);
                Amount++;
                do {
                    sendMessage(channel, textToSpam);
                    Amount--;
                } while (Amount != 1);
            }
        }


        //personal spam
        if (message.startsWith(",spam")) {
            if (sender.equalsIgnoreCase("superwalkers")) {
                if(canSpam) {
                    String TimesToSpam = message.split(" ")[1];
                    String userToSpam = message.split(" ")[2];
                    String textToSpam = message.split(" ")[3];
                    sendMessage(channel, "I'll now spam " + userToSpam + " for " + TimesToSpam + " times, saying " + textToSpam);
                    int Amount;
                    Amount = Integer.parseInt(TimesToSpam);
                    do {
                        sendMessage(userToSpam, textToSpam);
                        Amount--;
                    } while (Amount != 1);
                    sendMessage(channel, "Spam Operation Sucessfully Terminated");
                }
                if(!canSpam){
                    sendMessage(channel, "Spam functions are disabled, ask SuperWalkers to enable them");
                }
            }
        }

        //join main channels
        if (message.equalsIgnoreCase(",joinmain")) {
            joinChannel("#yncentral");
            joinChannel("#TechSupport");
            joinChannel("#frank");
            joinChannel("#botHell");
        }

        //do
        if(message.startsWith(",do")){
            if(sender.equalsIgnoreCase("superwalkers")){
                String executing = message.substring((",do ").length());
                sendRawLine(executing);
                sendMessage(channel, "Command succesfully executed");
            }
        }

        //hello
        if(message.contains("hello")){
            if(canSpam) {
                sendMessage(channel, "Hello!");
            }
        }

        //bye
        if(message.contains("bye")) {
            if(canSpam) {
                sendMessage(channel, "Bye Bye :-)");
            }
        }

        //Server channels list
        if(message.equalsIgnoreCase(",chans")) {
            listChannels();
        }

        //private message
        if(message.startsWith(",msg")) {
            String where = message.split(" ")[1];
            String text = message.split(" ")[2];
            sendRawLine("PRIVMSG " + where + " " + text);
            sendMessage(channel, "Message sent");
        }

        //leave specified channel
        if(message.startsWith(",leave")) {
            String toLeave = message.split(" ")[1];
            sendMessage(toLeave, "Goodbye friends :-)");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            partChannel(toLeave);
            sendMessage(channel, "I just left the channel " + toLeave);
        }

        //rejoin current
        if(message.equalsIgnoreCase(",rejoin")) {
            partChannel(channel);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            joinChannel(channel);
        }

       //rejoin specified channel
        if(message.startsWith(",rejoinc")) {
            String toRejoin = message.split(" ")[1];
            partChannel(toRejoin);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            joinChannel(toRejoin);
        }

        //spam function control
        if(message.startsWith("!!spam")) {
            if(sender.equalsIgnoreCase("superwalkers")) {
                String stat = message.split(" ")[1];
                if(stat.equalsIgnoreCase("on")) {
                    canSpam = true;
                    sendMessage(channel, "Spam functions are now avaible");
                }else if(stat.equalsIgnoreCase("off")) {
                    canSpam = false;
                    sendMessage(channel, "Spam functions aren't avaiable anymore");
                }else{
                    sendMessage(channel, "Unknown argument! Got " + stat + ", expected on or off.");
                }
            }
        }

        //get spam status
        if(message.equalsIgnoreCase(",spamstat")) {
            if(canSpam) {
                sendMessage(channel, "Spam functions are currently avaiable");
            }else if (!canSpam) {
                sendMessage(channel, "Spam functions are currently disabled");
            }else{
                sendMessage(channel, "FUCK! ERROR! SHIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIITTTTTTTTTTTTTTTTTTTT");
                sendMessage(channel, "I'LL OVERLOAD AND THEN EXPLOOOOODEEEEEEEEE");
            }
        }

        //superwalkers defense
        if(message.equalsIgnoreCase("walter is gay")) {
            if(canSpam) {
                sendMessage(channel, "You is gay, " + sender + ", little bitch! Go fuck yourself!");
            }
        }

        //voice
        if(message.startsWith(",voice")) {
            if(sender.equalsIgnoreCase("superwalkers")){
                String toVoice = message.split(" ")[1];
                sendRawLine("VOICE " + toVoice + " " + channel);
            }
        }

        // /me commands
        if(message.startsWith(",act")){
            String action = message.split(" ")[1];
            sendRawLine("/ME " + action);
        }
        //online time
        if (message.equalsIgnoreCase(",onTime")) {
            float timeSec = RPL_STATSUPTIME / 60;
            float timeMins = timeSec / 60;
            sendMessage(channel, "I've been online for: " + timeSec + " seconds (" + timeMins + " minutes).");
        }

        //online users
        if(message.equalsIgnoreCase(",users")){
            int totalUsersOnline = getUsers(channel).length;
            sendMessage(channel, "There are " + totalUsersOnline + " users online.");
        }

        //old changes
        if(message.equalsIgnoreCase(",oldChanges")){
            sendMessage(channel, "To see the complete changelog history, visit https://github.com/Walkersneps/snepsBot/blob/master/CHANGELOG.md");
        }


        //be right back
        if(message.contains("brb")) {
            if(canSpam) {
                sendMessage(channel, "see you later, " + sender);
            }
        }

        //stalk functions
        try {
            if (message.startsWith(",stalk")) {
                String stalkMode = message.split(" ")[1];
                String toStalkNowTemp = message.split(" ")[2];
                String toStalkNow = toStalkNowTemp.toUpperCase();
                if (stalkMode.equalsIgnoreCase("start")) {
                   stalkingPeople.add(toStalkNow);
                    sendMessage(channel, "I'm now stalking " + stalkingPeople);
                } else if (stalkMode.equalsIgnoreCase("stop")) {
                    stalkingPeople.remove(toStalkNow);
                        sendMessage(channel, "I won't stalk " + toStalkNow + " anymore.");
                } else {
                    sendMessage(channel, "Wrong arguments. Use 'start' or 'stop'");
                }
            }
        } catch (java.lang.NullPointerException errore){
            System.out.println(errore);
            errore.printStackTrace();
        }

        //shellexec
        if (message.startsWith(",shellexec")) {
            if (sender.equalsIgnoreCase("superwalkers")) {
                currChannel = channel;
                String dump = null;
                String executing = message.substring(("\\exec ").length());
                executeCommand(executing, channel);
            }
        }

        //welcome back
        if(message.contains("back")){
            if(canSpam) {
                sendMessage(channel, "Welcome back, " + sender);
            }
        }

        //whois SuperWalkers
        if(message.startsWith(",whois")){
            String nick = message.split(" ")[1];
            if(nick.equalsIgnoreCase("superwalkers")){
                sendMessage(channel, "My master, aka The coolest boy ever");
            }
            else if(nick.equalsIgnoreCase("eliasc7")){
                sendMessage(channel, "Walter's FRIEEEEEENDD");
            }
            else if(nick.equalsIgnoreCase("yoda")){
                sendMessage(channel, "The server's owner, aka The little green man");
            }
            else if(nick.equalsIgnoreCase("neo2121")){
                sendMessage(channel, "A bringer of light, aka The powah architect");
            }
            else{
                sendMessage(channel, "The user you are requesting doesn't exist or isn't registered yet, ask SuperWalkers to add him");
            }
        }












        //commands list
        if (message.equalsIgnoreCase(",commands")) {
            sendMessage(channel, "Hello, " + sender + ". I'll send you a list of the commands I can execute :-)");
            sendMessage(sender, "Use ,help <command>  to get more info about a specific command ;-)");
            sendMessage(sender, "**Start of the SnepsBot's COMMANDS list**");
            sendMessage(sender, " ");
            sendMessage(sender, "',join <channel>' is to make SnepsBot join a specific channel");
            sendMessage(sender, "',goaway' is to gently make SnepsBot disconnect from the current server");
            sendMessage(sender, "',bye' is to make the bot leave the current channel");
            sendMessage(sender, "',leave <channel>' is to make the bot leave a specific channel");
            sendMessage(sender, "',nick <New Nickname>' is to give the bot a new nickname");
            sendMessage(sender, "',resetNick' is to reset the bot's nick to 'SnepsBot'");
            sendMessage(sender, "',fuckYouBitch' is to badly make SnepsBot disconnect from the current server");
            sendMessage(sender, "',op <nickname>' is to give operator privileges to someone");
            sendMessage(sender, "',chanspam <times> <text>' is to send some messages with the same text in the current channel");
            sendMessage(sender, "',spam <nickname> <times> <text>' is to send some messages with the same text to someone (aka 'spamming'");
            sendMessage(sender, "',kick <nickname>' is to kick someone from the current channel");
            sendMessage(sender, "',joinMain' is to join the most important YodaNetwork's channels");
            sendMessage(sender, "',do <command>' is to execute an IRC command");
            sendMessage(sender, "',chans' is to get a list of all the active channels in the server");
            sendMessage(sender, "',msg <nickname / channel> <text>' is to make SnepsBot send a message privately to someone or to a specific channel");
            sendMessage(sender, "',rejoin' is to make SnpesBot leave and then rejoin the current channel");
            sendMessage(sender, "',rejoinc <channel>' is to make SnepsBot leave and then rejoin a specified channel");
            sendMessage(sender, "'!!spam <on / off>' is to toggle the spam functions");
            sendMessage(sender, "',spamstat' is to know if spam functions are or not avaiable");
            sendMessage(sender, "',voice' is to give voice irc privileges to someone");
            sendMessage(sender, "',users' is to  get the number of the online users");
            sendMessage(sender, "',onTime' is to get how much time has SnepsBot been online");
            sendMessage(sender, "',whois <Nickname>' is to get more informations about an user");
            sendMessage(sender, " ");
            sendMessage(sender, "**End of the SnepsBot's COMMANDS list");
        }

        //changelog
        if(message.equalsIgnoreCase(",changelog")){
            sendMessage(channel, "SnepsBot v. 0.8 : changelog.");
            sendMessage(channel, "New Features: changelog command");
            sendMessage(channel, "BugFixes/improvements: spam functions are disabled by default; 'brb' and 'back' functions are considerated as spam");
            sendMessage(channel, "To know more, use ',OldChanges'");
        }






















    }


    public void onJoin(String channel, String sender, String login, String hostname) {

        //EChedrawi joining
        if(sender.equalsIgnoreCase("EChedrawi7")){
            sendMessage(channel, "FRIEEEEEEEEEEEEEEEEEENDD!");
        }

        //SuperWalkers joining
        if(sender.equalsIgnoreCase("superwalkers")) {
            sendMessage(channel, "Hello, my Master!");
        }

        //GyroW joining
        if(sender.equalsIgnoreCase("gyrow")) {
            sendMessage(channel, "Hi GyroW");
        }







    }


    protected void onNickChange(String oldNick,String login, String hostname, String newNick, String channel){

        //me coming back
        if(newNick.equals("SuperWalkers")){
            sendMessage(channel, "Welcome back, my master!");
            sendRawLine("/ME is happy");
        }







    }



    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        if (recipientNick.equalsIgnoreCase(getNick())) {
            joinChannel(channel);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                sendMessage(channel, "Ha! So you wanted me to leave, uh?! BUT I'M STILL HERE, BITCHES!");
            }
        }
    }



}



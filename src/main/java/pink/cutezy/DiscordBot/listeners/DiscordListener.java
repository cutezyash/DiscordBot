package pink.cutezy.DiscordBot.listeners;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pink.cutezy.DiscordBot.DiscordPlugin;
import pink.cutezy.DiscordBot.commands.discord.ICMD;

import javax.annotation.Nonnull;

public class DiscordListener extends ListenerAdapter {
    private DiscordPlugin plugin;
    public DiscordListener(DiscordPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(event.getAuthor().isBot() || event.getAuthor().isFake())
            return;
        String raw = event.getMessage().getContentRaw();
        if(raw.startsWith("!")) {
            String[] split = raw.split(" ");
            ICMD ICMD = plugin.getCmdOut(split[0].substring(1));
            if(ICMD != null) {
                if(split.length > 1)
                    System.arraycopy(split, 0, split, 1, raw.length());
                ICMD.Invoke(event.getMessage(), split);
                return;
            }
        }
        MessageChannel channel = event.getChannel();
        if(!channel.getId().equals(DiscordPlugin.getConfig().channelId))
            return;
        User author = event.getAuthor();
        DiscordPlugin.sendIn(author, event.getMessage().getContentStripped());
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        super.onReady(event);
        plugin.onReady();
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        super.onShutdown(event);
        plugin.onShutdown();
    }
}

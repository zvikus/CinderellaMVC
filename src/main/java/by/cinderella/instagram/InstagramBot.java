package by.cinderella.instagram;


//import com.xcoder.easyinsta.Instagram;
//import com.xcoder.tasks.AsyncTask;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//@Configuration
//@EnableScheduling
public class InstagramBot {


//    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
//    public void checkSubscriptions() throws Exception {
//        Instagram.Actions actions  = instagram.actions();
//        Instagram.Direct direct  = instagram.direct();
//        Instagram.Profile profile  = instagram.profile();
//
//        AsyncTask<List<String>> followers = profile.getFollowers("cinderella.minsk");
//        List<String> result = followers.getResult(5);
//
//        AsyncTask<Void> directMessage = direct.directMessage("cvichkovski","Hey there !");
//
//
//
//
//        direct.attachNotificationListener(message -> {
//            // A new message has been received, lets reply to it
//            if (message.text.equals("4")) {
//                message.reply("Hello, I'm a bot");
//            }
//        });
//    }

}

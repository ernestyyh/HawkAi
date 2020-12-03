import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.vdurmont.emoji.EmojiParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HawkAi_bot extends TelegramLongPollingBot {

    private mapOfHawker mapHawker = new mapOfHawker();
    private ArrayList<Hawker> print = new ArrayList<>();
    private mapOfCoordinates mapCoordinates = new mapOfCoordinates();

    private static double distanceCal(Location loc, Hawker hawker) {
        double lat1 = loc.getLatitude();
        double lon1 = loc.getLongitude();
        return distanceCal(lat1, lon1, hawker);
    }

    private static double distanceCal(double lat1, double lon1, Hawker hawker) {
        double lat2 = hawker.getLat();
        double lon2 = hawker.getLon();
        double PI_d180 = 0.017453292519943295;
        double interim = 0.5 - Math.cos((lat2 - lat1) * PI_d180)/2 + Math.cos(lat1 * PI_d180) * Math.cos(lat2 * PI_d180) * (1 - Math.cos((lon2 - lon1) * PI_d180))/2;

        return 12742 * Math.asin(Math.sqrt(interim));
    }

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (message_text.equals("/start")) {

                SendMessage startMessage = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Thanos SNAP! I still here. I'm always here :eyes:"));
                SendPhoto wow = new SendPhoto().setChatId(chat_id).setPhoto(new File("src/main/resources/EyeKnow.jpg"));
                SendMessage whereMessage = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Where you now? :confused:"));
                ReplyKeyboardMarkup locationMarkUp = new ReplyKeyboardMarkup().setResizeKeyboard(true).setOneTimeKeyboard(true);
                List<KeyboardRow> onlyLocation = new ArrayList<>();
                KeyboardRow rowLocation = new KeyboardRow();
                rowLocation.add(new KeyboardButton(EmojiParser.parseToUnicode("Send Location :round_pushpin:")).setRequestLocation(true));
                onlyLocation.add(rowLocation);
                locationMarkUp.setKeyboard(onlyLocation);
                whereMessage.setReplyMarkup(locationMarkUp);

                try {
                    execute(startMessage);
                    execute(wow);
                    execute(whereMessage); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.length() == 6) {

                boolean isNumber = true;
                for (char c : message_text.toCharArray()) {
                    if (Character.isLetter(c)) {
                        isNumber = false;
                        break;
                    }
                }

                if (!isNumber) {
                    SendMessage invalid = new SendMessage().setChatId(chat_id).setText("Wrong liao!");

                    try {
                        execute(invalid);

                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    return;
                }

                Coordinates loc = mapCoordinates.getCoordinates(message_text);

                if (loc != null) {
                    double lat1 = loc.getLat();
                    double lon1 = loc.getLon();
                    HashMap<String, Hawker> check = mapHawker.getMap();

                    for (Hawker current : check.values()) {
                        if (distanceCal(lat1, lon1, current) <= 1.5) {
                            print.add(current);
                        }
                    }

                    StringBuilder storeMessage = new StringBuilder();

                    storeMessage.append(EmojiParser.parseToUnicode("Ah boy, Ah girl, hungry? :stew:\n\n"));
                    int stdLength = storeMessage.length();

                    for (Hawker current : print) {
                        storeMessage.append(current);
                    }

                    SendMessage listMessage = new SendMessage().setChatId(chat_id).setText(storeMessage.toString());

                    print.clear();

                    SendMessage chooseMessage = null;

                    if (storeMessage.length() != stdLength) {

                        try {
                            execute(listMessage);

                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }

                        chooseMessage = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Faster choose one :alarm_clock:"));

                        try {
                            execute(chooseMessage);

                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        chooseMessage = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Why your place so ulu one? :anguished:"));
                        SendMessage makan = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Not so ulu then ask again"));

                        try {
                            execute(chooseMessage);
                            execute(makan);

                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    SendMessage chooseMessage = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Smells like Malaysia :new_moon_with_face:"));
                    SendMessage makan = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Cross the causeway then ask again"));

                    try {
                        execute(chooseMessage);
                        execute(makan);

                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            } else if (message_text.length() == 7) {
                String postalCode = message_text.substring(1);

                boolean isNumber = true;
                for (char c : postalCode.toCharArray()) {
                    if (Character.isLetter(c)) {
                        isNumber = false;
                        break;
                    }
                }

                if (!isNumber) {
                    SendMessage invalid = new SendMessage().setChatId(chat_id).setText("Wrong liao!");

                    try {
                        execute(invalid);

                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    return;
                }

                Hawker chosen = mapHawker.getHawker(postalCode);

                SendLocation locInfo = new SendLocation().setChatId(chat_id).setLatitude((float) chosen.getLat()).setLongitude((float) chosen.getLon());
                SendMessage question = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Makan here or choose again? :fork_and_knife:"));

                try {
                    execute(locInfo); // Sending our message object to user
                    execute(question);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.equals("/help")) {
                SendMessage help = new SendMessage().setChatId(chat_id).setText("Press \"Send Location\" button or type in a postal code (eg, 123456) to get locations of hawker centres in the vicinity");

                try {
                    execute(help);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

        } else if (update.hasMessage() && update.getMessage().hasLocation()) {

            long chat_id = update.getMessage().getChatId();
            Location locInfo = update.getMessage().getLocation();
            HashMap<String, Hawker> check = mapHawker.getMap();

            for (Hawker current : check.values()) {
                if (distanceCal(locInfo, current) <= 1.5) {
                    print.add(current);
                }
            }

            StringBuilder storeMessage = new StringBuilder();

            storeMessage.append(EmojiParser.parseToUnicode("Ah boy, Ah girl, hungry? :stew:\n\n"));
            int stdLength = storeMessage.length();

            for (Hawker current : print) {
                storeMessage.append(current);
            }

            SendMessage listMessage = new SendMessage().setChatId(chat_id).setText(storeMessage.toString());

            print.clear();

            SendMessage chooseMessage = null;

            if (storeMessage.length() != stdLength) {
                try {
                    execute(listMessage);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                chooseMessage = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Faster choose one :alarm_clock:"));

                try {
                    execute(chooseMessage);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                chooseMessage = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Why your place so ulu one? :anguished:"));
                SendMessage makan = new SendMessage().setChatId(chat_id).setText(EmojiParser.parseToUnicode("Not so ulu then ask again"));

                try {
                    execute(chooseMessage);
                    execute(makan);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "HawkAi_bot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "637894843:AAEhhRwnPR8O4y-xl_gLELh-BwKCsO-KRQE";
    }
}
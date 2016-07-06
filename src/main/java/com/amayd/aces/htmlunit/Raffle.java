package com.amayd.aces.htmlunit;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Raffle {

    final WebClient webClient = new WebClient();
    final static String ACES_MAIN = "http://aces.gg";
    private HtmlPage p;
    private WebRequest requestSettings;
    private String raffleURI;
    private int counter;

    public void openMainPage(String url) throws IOException {
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);

        webClient.getPage(url);
    }

    public void logIn(String name, String password) throws IOException {
        requestSettings = new WebRequest(
                new URL(ACES_MAIN), HttpMethod.POST);

        requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
        requestSettings.getRequestParameters().add(new NameValuePair("login_name", name));
        requestSettings.getRequestParameters().add(new NameValuePair("login_password", password));
        requestSettings.getRequestParameters().add(new NameValuePair("login","submit"));

        webClient.getPage(requestSettings);
    }

    private HtmlPage openRaffle() throws IOException {
        requestSettings = new WebRequest(new URL(ACES_MAIN + "/engine/ajax/tournament.php/?act=drawings&drawing_tmpl=drawings_box&drawing_fix_limit=5&drawing_number_on_1_page=5&drawing_f_obj_type=6&drawing_f_obj_id=8&drawing_f_completed=-1&drawing_r_act=%2Findex.php%3Fdo%3Dstreams%26stream%3D8&_=" + Long.toString(System.currentTimeMillis())), HttpMethod.GET);

//        requestSettings.setRequestParameters(getPopulatedGetParameters());

        return webClient.getPage(requestSettings);
//        List<?> list = p.getByXPath("//button");
//        HtmlButton ruffleButton = (HtmlButton) list.get(0);
//        raffleURI = ruffleButton.getAttribute("data-url");
    }

    private boolean isRuffleOpened() {
        List<?> list = p.getByXPath("//button");
        if (list.size() > 0){
            HtmlButton ruffleButton = (HtmlButton) list.get(0);
            raffleURI = ruffleButton.getAttributesMap().get("data-url").getValue();
            return true;
        }

        return false;
    }

    private void attendRaffle() throws IOException {
        requestSettings = new WebRequest(new URL(ACES_MAIN + raffleURI + "&_=" + Long.toString(System.currentTimeMillis())), HttpMethod.GET);

//        requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
//        requestSettings.getRequestParameters().add(new NameValuePair("_", Long.toString(System.currentTimeMillis())));

        p = webClient.getPage(requestSettings);
//        String s = p.getWebResponse().getContentAsString("UTF-8");
        List<?> list = p.getByXPath("//div[2]/div[2]/div[3]/div/div/div[2]/div/div");
        if(list.size() > 0){
//            counter++;
            System.out.println("You are in " + raffleURI.substring(raffleURI.length() - 22, raffleURI.length() - 18));
        }
    }

    private void getRaffleStatus() {

        List<?> list = p.getByXPath("//div[2]/div[2]/div[3]/div/div/div[2]/div/div");
        if(list.size() > 0){
            list.get(0);

        }
        System.out.println("closed");
    }

    private List<NameValuePair> getPopulatedGetParameters(){
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        paramList.add(new NameValuePair("act","drawings"));
        paramList.add(new NameValuePair("drawing_tmpl","drawings_box"));
        paramList.add(new NameValuePair("drawing_fix_limit","5"));
        paramList.add(new NameValuePair("drawing_number_on_1_page","5"));
        paramList.add(new NameValuePair("drawing_f_obj_type","6"));
        paramList.add(new NameValuePair("drawing_f_obj_id","8"));
        paramList.add(new NameValuePair("drawing_f_completed","-1"));
        paramList.add(new NameValuePair("drawing_r_act","/index.php?do=streams&stream=8"));
        paramList.add(new NameValuePair("_", Long.toString(System.currentTimeMillis())));

        return paramList;
    }

    public static void main(String[] args) {
        Raffle raffle = new Raffle();
        try {
            while (true){
                raffle.openMainPage(ACES_MAIN);
                raffle.logIn("Andy_if", "159357");
                raffle.counter++;
                raffle.p = raffle.openRaffle();
                if(raffle.isRuffleOpened()){
                    raffle.attendRaffle();
                }else {
                    if(raffle.counter % 10 == 0){
                        System.out.println(".");
                    }
                }
                Thread.sleep(60000);
            }
        }catch (IOException e){

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

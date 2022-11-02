package it.unimib.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity di esempio per mostrare alcuni concetti base.
 */
public class MainActivity extends AppCompatActivity {

    // Costante che assume come valore il nome della classe da utilizzare per le stampe di log
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associazione del file di layout activity_main.xml all'Activity corrente (MainActivity)
        setContentView(R.layout.activity_main);

        // Ottengo il riferimento alla TextView tv_hello_world_title contenuta nel layout
        // res/layout/activity_main.xml
        TextView tvHelloWorldTitle = findViewById(R.id.tv_hello_world_title);

        applyBlurEffect(tvHelloWorldTitle);

        // Ottengo il riferimento al bottone btn_send_message
        Button btnSendMessage = findViewById(R.id.btn_send_message);

        /*
         * Associazione di un listener al bottone quando viene premuto - Metodo 1
         *
         * Modo classico per associare un listener ad una View utilizzando le classi anonime.
         * Il metodo setOnClickListener(View.OnClickListener) accetta come parametro un oggetto di
         * tipo View.OnClickListener che non è altro che un'interfaccia definita all'interno della
         * classe View dell'SDK di Android:
         * https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/java/android/view/View.java#29551
         */
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Il bottone per inviare un messaggio è stato premuto");
            }
        });

        /*
         * Associazione di un listener al bottone quando viene premuto - Metodo 2
         *
         * Versione più compatta utilizzando una lambda expression di Java:
         *
         * btnSendMessage.setOnClickListener(btn -> {
         *  Log.d(TAG, "Il bottone per inviare un messaggio è stato premuto");
         * });
         *
         * La sintassi mostrata funziona perché l'interfaccia View.OnClickListener ha un solo metodo
         * per cui è necessario fare l'override: onClick(View).
         *
         * Lambda expression sintassi:
         * (argomentoMetodoInterfaccia - si può scegliere il nome che si vuole) -> {corpo del metodo}
         *
         * Se il metodo dell'interfaccia ha più di un argomento, vanno specificati tanti nomi quanti
         * sono gli argomenti, separati da una virgola.
         *
         * Se il metodo non ha parametri, la sintassi sarà la seguente: () -> {}
         *
         * Se l'interfaccia avesse avuto due o più metodi, non sarebbe stato possibile utilizzare
         * la lambda expression. Questo costrutto funziona infatti solamente con le interfacce che
         * hanno un solo metodo astratto, e vengono chiamate Functional Interface.
         */

        // Ottengo il riferimento al bottone btn_change_text
        Button btnChangeText = findViewById(R.id.btn_change_text);

        /*
         * Associazione di un listener al bottone quando viene premuto - Solamente da vedere come
         * esempio, ma da non utilizzare in pratica
         *
         * Esempio per spiegare quello che sta dietro all'utilizzo delle classi anonime.
         *
         * Per evitare di dover creare una variabile, assegnare ad essa un valore e poi passarla come
         * argomento del metodo onClick(View), si ricorre all'utilizzo delle classi anonime.
         *
         * La sintassi mostrata sopra, in una forma leggermente meno compatta, diventa così:
         *
         * View.OnClickListener listener = new View.OnClickListener() {
         *   @Override
         *   public void onClick(View v) {
         *       Log.d(TAG, "Il bottone per inviare un messaggio è stato premuto");
         *   }
         * };
         * btnSendMessage.setOnClickListener(listener);
         *
         * Nella forma più estesa, ciò che avviene quando si associa un listener ad una
         * View utilizzando una classe anonima corrisponde ad avere una classe concreta che
         * implementa l'interfaccia View.OnClickListener, e quindi fa l'override del metodo
         * onClick(View).
         *
         * MyOnClickListener è una classe che ho definito in questo file, e che
         * implementa l'interfaccia View.OnClickListener.
         *
         * Utilizzando una classe anonima, quindi, si evita di dover dichiarare e istanziare una
         * classe ad hoc, come nel caso dell'esempio con MyOnClickListener, con conseguente
         * risparmio di numerose righe di codice.
         */
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        btnChangeText.setOnClickListener(myOnClickListener);
    }

    /**
     * Metodo che sfoca la view che viene passata come argomento.
     * @param view la View a cui applicare la sfocatura.
     */
    private void applyBlurEffect(View view) {
        /*
         * Esempio per capire il comportamento di minSDK e come gestirlo:
         *
         * tvHelloWorldTitle.setRenderEffect(
                RenderEffect.createBlurEffect(10.0f, 10.0f, Shader.TileMode.CLAMP));
         *
         * I metodi setRenderEffect(RenderEffect) e createBlurEffect(float, float, Shader.TileMode)
         * sono stati introdotti con le API 31
         * (https://developer.android.com/about/versions/12/features#rendereffect).
         *
         * Avendo impostato il valore minSDK pari a API 22, Android Studio segnala il problema,
         * ma lascia comunque compilare l'applicazione.
         *
         * Il risultato è il seguente:
         *
         * - Se eseguite l'applicazione su un dispositivo che ha una version di Android inferiore
         *   alla 12 (API 31), l'applicazione si arresterà a causa di un crash (otterrete l'eccezione
         *   java.lang.NoClassDefFoundError: Failed resolution of: Landroid/graphics/RenderEffect [..])
         *
         * - Se eseguite l'applicazione su un dispositivo che ha Android 12 o superiore, la TextView
         *   che contiene il messaggio Hello World verrà sfocata.
         */

        /*
         * Per gestire questi casi particolari, bisogna ricorrere alla seguente strategia:
         *
         * Si determina qual è la versione di Android installata sul dispositivo attraverso
         * l'istruzione Build.VERSION.SDK_INT e si verifica che sia maggiore o uguale al livello
         * di API a partire dal quale la funzionalità è supportata (in questo caso, API 31 -
         * Build.VERSION_CODES.S).
         *
         * In questo modo, la sfocatura verrà applicata solamente sulle versioni di Android a
         * partire dalla 12 (API 31), mentre sulle altre versioni di Android la sfocatura non verrà
         * applicata, ma l'applicazione potrà comunque funzionare senza problemi.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            view.setRenderEffect(
                    RenderEffect.createBlurEffect(10.0f, 10.0f, Shader.TileMode.CLAMP));
        }
    }

    /**
     * Esempio di classe che implementa l'interfaccia OnClickListener definita all'interno della
     * classe View.
     */
    public static class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Il bottone per cambiare il testo è stato premuto");
        }
    }
}

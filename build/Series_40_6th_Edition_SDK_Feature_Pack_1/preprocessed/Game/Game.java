/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Game;

import java.io.IOException;
import java.util.Random;
import javax.microedition.media.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


/**
 * @author Manoj
 */
public class Game extends MIDlet {
    private Display display;
    private GameCanvas gameCanvas;
    public Game()
    {
        display=Display.getDisplay(this);
        gameCanvas=new GameCanvas(this);
    }
    public void startApp() {
        display.setCurrent(gameCanvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
    public void exitMIDlet()
    {
    destroyApp(true);
    notifyDestroyed();
    }
}

class GameCanvas extends Canvas implements CommandListener
{
    private Game game2;
    private  Command exit;
    public int x1,y1,x2,y2;
    public int note=60;
    private Player player1;
    private boolean startFlag;
    public Random random;
    int arr[][]=new int[5][5];
    boolean Arrflag[][]=new boolean[5][5];
    int problemArr[]=new int[5];
    public int timer=0;
    boolean arrChar[]=new boolean[26];
    int arrX[]=new int[26];
    int arrY[]=new int[26];
    int SPEED=2;
    int count=0,problemChar,score;
    String status="";
    int animationX=getWidth()/2;
    boolean animationFlag,loading;
    int a1,a2,loadingX;
    public GameCanvas(Game game2)
    {
        this.game2=game2;
        exit = new Command("Exit", Command.EXIT, 1);
        addCommand(exit);
        setCommandListener(this);
            try {
                player1 = Manager.createPlayer(getClass().getResourceAsStream("s.mp3"), "audio/mpeg");
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (MediaException ex) {
                ex.printStackTrace();
            }
            startFlag=false;
            try {
            player1.start();
        } catch (MediaException ex) {
        }
            random=new Random();
           
            for(int i=0;i<arrX.length;i++)
        {
            arrX[i]=randomInRange(getWidth()-20);
            arrY[i]=randomInRange(getHeight()-50);
        }
        problemChar=(randomInRange(26)+'A');
        score=0;
        status="search chars";
        animationFlag=false;
        loading=false;
        loadingX=0;
    }
    protected void paint(Graphics g) {
        int w=getWidth();
        int h=getHeight();

        if(!startFlag)
        {   g.setColor(0,0,0);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Starting Animation
           //g.drawImage(image, getWidth() / 2, getHeight() / 2, Graphics.HCENTER| Graphics.VCENTER);

            

           g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_ITALIC, Font.SIZE_LARGE));
           g.setColor(0,255,0);

           // boundry
           for(int i=0;i<5;i++)
           g.drawRect(10+i,10+i,w-20-2*i,h-20-2*i);
           // end boundry

           g.drawString("YGB GAMES",animationX, getWidth()/2, Graphics.LEFT | Graphics.TOP);
           g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
           if(randomInRange(10)==0)
           {
               a1=randomInRange(255);
               a2=randomInRange(255);
           }
           g.setColor(a1,a2,a1);
           g.drawString("Touch Game", w/2-50, h/2-80, Graphics.LEFT | Graphics.TOP);

           if(loading)
           {
           g.setColor(a2,a1,a2);
           g.drawString("Click To Continue", w/2-60, h/2+50, Graphics.LEFT | Graphics.TOP);
           }
           else
           {
           // loading bar
           g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL));
           g.setColor(0,0,255);
           g.drawRect(25,getHeight()-50,getWidth()-50,20);
           loadingX+=randomInRange(5);
           g.fillRect(27,getHeight()-48,loadingX, 16);
           if(loadingX>=getWidth()-54)
            loading=true;
           g.setColor(0,255,0);
           String p=(int)((float)loadingX/(getWidth()-54)*100)+"%";
           g.drawString(p, getWidth()/2, getHeight()-47,Graphics.LEFT | Graphics.TOP );
           //loading end
            }

           if(animationFlag)
               animationX+=1;
           else
               animationX-=1;
           if(animationX>getWidth()-120)
               animationFlag=false;
           if(animationX<20)
               animationFlag=true;
           delay(10);
        }
        else
        {
            g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
            // Game Coding
            g.setColor(255,255,255);
        g.fillRect(0,0,getWidth(),getHeight());

        for(int i=0;i<arrX.length;i++)
        {   if(!arrChar[i])
            {
            g.setColor(randomInRange(255),randomInRange(255),randomInRange(255));
            //g.drawRect(arrX[i], arrY[i], 20, 20);
            g.fillRect(arrX[i], arrY[i], 20, 20);
            g.setColor(randomInRange(255),randomInRange(255),randomInRange(255));
            g.drawString((char)('A'+i)+"", arrX[i]+2, arrY[i], Graphics.LEFT|Graphics.TOP);
            }
        }
        if(status.equals("Game Over"))
        {
            g.setColor(255,0,0);
            if(score>25)
            g.drawString("You Won D Game",getWidth()/2-50, getHeight()/2,Graphics.LEFT|Graphics.TOP );
            else
            g.drawString("You Lost D Game",getWidth()/2-50, getHeight()/2,Graphics.LEFT|Graphics.TOP );
        }
        else
        {
        g.setColor(255,0,0);
        g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL));
        g.drawLine(10,getHeight()-30,10,20);
        g.drawLine(10,20,getWidth()-10,20);
        g.drawLine(getWidth()-10,20,getWidth()-10,getHeight()-30);

        g.drawLine(10,getHeight()-30,getWidth()-10,getHeight()-30);
        g.drawLine(10,getHeight()-31,getWidth()-10,getHeight()-31);

        g.setColor(0,0,255);
        g.drawRect(30,getHeight()-25,20,20);
        g.drawString((char)problemChar+"",35, getHeight()-23,Graphics.LEFT|Graphics.TOP );

        g.drawString("score : "+score+"",getWidth()-70, getHeight()-20,Graphics.LEFT|Graphics.TOP );

        g.drawString(status,getWidth()/2-50, getHeight()-20,Graphics.LEFT|Graphics.TOP );


        updatingPoints();
        }
            delay(50);
        }
        delay(10);
        repaint();
    }

       public void updatingPoints()
    {
        // updating points
        for(int i=0;i<arrX.length;i++)
        {
            // for X Coordinate
            if(randomInRange(2)==0)
            {   arrX[i]-=SPEED;
                if(arrX[i]<20)
                    arrX[i]+=SPEED;
            }
            else
            {   arrX[i]+=SPEED;
                if(arrX[i]>getWidth()-30)
                    arrX[i]-=SPEED;
            }
            // for Y Coordinate
            if(randomInRange(2)==0)
            {   arrY[i]-=SPEED;
                if(arrY[i]<20)
                    arrY[i]+=SPEED;
            }
            else
            {   arrY[i]+=SPEED;
                if(arrY[i]>getHeight()-50)
                    arrY[i]-=SPEED;
            }
        }

    }
     public boolean blank()
    {   int count1=0;
        for(int i=0;i<arrChar.length;i++)
            if(arrChar[i])
                count1++;
        if(count1>20)
            return true;
        return false;
    }
    public void selectNewProblemChar()
    {   int i=randomInRange(26);

        while(arrChar[i])
            i=randomInRange(26);
        problemChar=(i+'A');
    }
    public int randomInRange(int limit)
    {
        int i=random.nextInt()%limit;
        if(i<0)
            return i+limit;
        return i;
    }
    public void delay(int time)
    {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }
    public void commandAction(Command c, Displayable d) {
        if (c == exit)
      game2.exitMIDlet();

    }
    public void makeSound(int note)
    {
        try { Manager.playTone(note, 100, 100); }
        catch (MediaException me) {}

    }
   protected void pointerPressed(int x, int y)
  {
        if(!startFlag && loading)
        {   startFlag=true;
            try {
            player1.stop();
        } catch (MediaException ex) {
            ex.printStackTrace();
        }
            try {
                player1 = Manager.createPlayer(getClass().getResourceAsStream("p.mp3"), "audio/mpeg");
                player1.setLoopCount(5);
                player1.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (MediaException ex) {
                ex.printStackTrace();
            }


        }
        else if(startFlag)
        {
            for(int i=0;i<arrX.length;i++)
            {
            if(!arrChar[i] && (x>arrX[i] && x<arrX[i]+20) && (y>arrY[i] && y<arrY[i]+20) )
            {
                arrChar[i] = true;
                if(i+'A'==problemChar)
                {
                    score += 10;
                    status="Correct";
                    selectNewProblemChar();
                    makeSound(60);

                }
                else
                {
                    score -= 15;
                    makeSound(90);
                    status="Wrong";
                }
            }
        }
             if(blank())
             {
                 status = "Game Over";
                 for(int i=0;i<arrChar.length;i++)
                 {
                     arrChar[i]=true;
                 }
            }
       }
       repaint();
  }
  protected void pointerDragged(int x, int y)
  {x2=x;y2=y;
    repaint();
  }
}


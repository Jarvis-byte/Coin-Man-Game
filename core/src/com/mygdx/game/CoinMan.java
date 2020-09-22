package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
private Music music;
	SpriteBatch batch;
	Texture background;
	Texture[]man;
	Texture dizzy;
	int manstate=0;
	int pause=0;
	float gravity=0.2f;
	float velocity = 0;
	int manY=0;
	Random random;
int Score=0;
BitmapFont font;
	Rectangle manRectangle;
	ArrayList<Integer> coinXs=new ArrayList<Integer>();
	ArrayList<Integer> coinYs=new ArrayList<Integer>();
	ArrayList<Rectangle>coinRectangles=new ArrayList<>();
	ArrayList<Rectangle>bombRectangles=new ArrayList<>();
	Texture coin;
	int coinCount;
	int gameState=0;
	ArrayList<Integer> bombXs=new ArrayList<Integer>();
	ArrayList<Integer> bombYs=new ArrayList<Integer>();
	Texture bomb;
	int bombCount;
	@Override
	public void create () {
		batch = new SpriteBatch();
		music=Gdx.audio.newMusic(Gdx.files.internal("bckmusic.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
		background=new Texture("bg.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
dizzy=new Texture("dizzy-1.png");
		manY=Gdx.graphics.getHeight()/2;
		coin=new Texture("coin.png");
		bomb=new Texture("bomb.png");
		random=new Random();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(7);

	}
public void makeCoin()
{
	float height=random.nextFloat()*Gdx.graphics.getHeight();
	coinYs.add((int)height);
	coinXs.add(Gdx.graphics.getWidth());
 }
	public void makeBomb()
	{
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		bombYs.add((int)height-40);
		bombXs.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {
	batch.begin();
	batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
	if(gameState==1)
	{
		//Game is Live;
		//BOMBS
		if (bombCount<250)
		{
			bombCount++;
		}
		else {
			bombCount=0;
			makeBomb();
		}
		bombRectangles.clear();
		for (int i =0;i<bombXs.size();i++)
		{
			batch.draw(bomb,bombXs.get(i),bombYs.get(i));
			bombXs.set(i,bombXs.get(i)-8);
			bombRectangles.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
		}


		//COINS
		if (coinCount<100)
		{
			coinCount++;
		}
		else {
			coinCount=0;
			makeCoin();
		}
		coinRectangles.clear();
		for (int i =0;i<coinXs.size();i++)
		{
			batch.draw(coin,coinXs.get(i),coinYs.get(i));
			coinXs.set(i,coinXs.get(i)-4);
			coinRectangles.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));

		}

		if (Gdx.input.justTouched())
		{
			velocity=-10;
		}

		if(pause<8)
		{
			pause++;

		}

		else{
			pause=0;
			if(manstate<3)
			{
				manstate++;
			}else
			{
				manstate=0;
			}

		}

		velocity=velocity+gravity;
		manY -=velocity;
		if (manY<=0)
		{
			manY=0;
		}

	}
	else if(gameState==0)
	{
		//Waiting to start
		if (Gdx.input.justTouched())
		{
			gameState=1;

		}
	}
	else if (gameState==2)
	{
		//game Over
		if (Gdx.input.justTouched())
		{
			gameState=1;
			manY=Gdx.graphics.getHeight()/2;
			Score=0;
			velocity=0;
			coinXs.clear();
			coinYs.clear();
			coinRectangles.clear();
			coinCount=0;
			bombXs.clear();
			bombYs.clear();
			bombRectangles.clear();
			bombCount=0;

		}

	}

if (gameState==2)
{
	batch.draw(dizzy,Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);


}else
{
	batch.draw(man[manstate],Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);
}

	manRectangle=new Rectangle(Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY,man[manstate].getWidth(),man[manstate].getHeight());
	for (int i=0;i<coinRectangles.size();i++)
	{
		if (Intersector.overlaps(manRectangle,coinRectangles.get(i)))
		{
			Score++;
			coinRectangles.remove(i);
			coinXs.remove(i);
			coinYs.remove(i);
			break;
		}

	}
		for (int i=0;i<bombRectangles.size();i++)
		{
			if (Intersector.overlaps(manRectangle,bombRectangles.get(i)))
			{
					gameState=2;
		}

		}
		font.draw(batch,String.valueOf(Score),100,200);
	batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		music.dispose();

	}
}

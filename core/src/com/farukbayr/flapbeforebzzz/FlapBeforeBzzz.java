package com.farukbayr.flapbeforebzzz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlapBeforeBzzz extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture bee1;
	Texture bee2;
	Texture bee3;
	float birdX = 0;
	float birdY = 0;
	float gravity = 0.1f;
	float velocity = 0;
	float enemyVelocity = 3;
	float distance = 0;
	int score = 0;
	int scoredEnemy = 0;
	int gameState = 0;
	int numberOfEnemies = 4;
	float [] enemyX = new float[numberOfEnemies];
	float [] enemyOffSet1 = new float[numberOfEnemies];
	float [] enemyOffSet2 = new float[numberOfEnemies];
	float [] enemyOffSet3 = new float[numberOfEnemies];
	Random random;
	Circle birdCircle;
	Circle [] enemyCircles1;
	Circle [] enemyCircles2;
	Circle [] enemyCircles3;
	ShapeRenderer shapeRenderer;
	BitmapFont font;
	BitmapFont font2;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		bee1 = new Texture("bee.png");
		bee2 = new Texture("bee.png");
		bee3 = new Texture("bee.png");

		distance = Gdx.graphics.getWidth() / 2;

		random = new Random();

		birdX = Gdx.graphics.getWidth() / 3 - bird.getHeight() / 2;
		birdY = Gdx.graphics.getHeight() / 2;

		birdCircle = new Circle();
		enemyCircles1 = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3 = new Circle[numberOfEnemies];

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);

		font2 = new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(6);

		shapeRenderer = new ShapeRenderer();

		for (int i = 0; i < numberOfEnemies; i++) {
			enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

			enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i * distance;

			enemyCircles1[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}
	}

	@Override
	public void render () {
		batch.begin();

		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {
			if (enemyX[scoredEnemy] < Gdx.graphics.getWidth() / 3 - bird.getHeight() / 2) {
				score++;

				if (scoredEnemy < numberOfEnemies - 1) {
					scoredEnemy++;
				} else {
					scoredEnemy = 0;
				}
			}

			if (Gdx.input.justTouched()) {
				velocity = velocity - 8;
			}

			for (int i = 0; i < numberOfEnemies; i++) {
				if (enemyX[i] < Gdx.graphics.getWidth() / 25) {
					enemyX[i] = enemyX[i] + numberOfEnemies * distance;

					enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				} else {
					enemyX[i] = enemyX[i] - enemyVelocity * 2;
				}

				batch.draw(bee1, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet1[i], Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / 15);
				batch.draw(bee2, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet2[i], Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / 15);
				batch.draw(bee3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet3[i], Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / 15);

				enemyCircles1[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 50,  Gdx.graphics.getHeight() / 2 + enemyOffSet1[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 50);
				enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 50,  Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 50);
				enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 50,  Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 50);
			}

			if (birdY > 0 || velocity < 0) {
				velocity = velocity + gravity * 4;
				birdY = birdY - velocity;
			} else {
				gameState = 2;
			}
		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2) {
			font2.draw(batch, "Game Over! Tap To Play Again!", 100, Gdx.graphics.getHeight() / 2);

			if (Gdx.input.justTouched()) {
				gameState = 1;

				birdY = Gdx.graphics.getHeight() / 2;

				for (int i = 0; i < numberOfEnemies; i++) {
					enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

					enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i * distance;

					enemyCircles1[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}

				velocity = 0;
				scoredEnemy = 0;
				score = 0;

			}
		}

		batch.draw(bird, birdX, birdY, Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / 15);

		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();

		birdCircle.set(birdX + Gdx.graphics.getWidth() / 50, birdY + Gdx.graphics.getHeight() / 30, Gdx.graphics.getWidth() / 50);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.ROYAL);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < numberOfEnemies; i++) {
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 50,  Gdx.graphics.getHeight() / 2 + enemyOffSet1[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 50);
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 50,  Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 50);
			//shapeRenderer.circle(enemyX[i] + Gdx.graphics.getWidth() / 50,  Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 50);

			if (Intersector.overlaps(birdCircle, enemyCircles1[i]) || Intersector.overlaps(birdCircle, enemyCircles2[i]) || Intersector.overlaps(birdCircle, enemyCircles3[i])) {
				gameState = 2;
			}
		}

		//shapeRenderer.end();
	}

	@Override
	public void dispose () {

	}

}
package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Asteroids extends ApplicationAdapter {
	ShapeRenderer sr;
	SpriteBatch batch;
	BitmapFont font;
	
	Random random = new Random();
	//((Player)shapes[0][0]) ((Player)shapes[0][0]) = new ((Player)shapes[0][0])(new float[][] {{0, 0}, {20, 0}, {10, 40}});
	//Bullet[] bullets = new Bullet[0];
	float width = 640, height = 480;
	
	float[][][] asteroidVertices = new float[][][] {{{0, 0}, {-10, 10}, {-4, 20}, {0, 14}, {10, 18}, {12, 4}},
													{{-10, 0}, {10, 1}, {9, 10}, {6, 9}, {3, 20}, {-5, 16}}, 
													{{-12, -4}, {6, 2}, {8, 8}, {2, 19}, {-4, 18}, {-7, 5}, {-9, 7}}}; //all possible asteroids (3 currently) 
	int waveSize = 2;
	
	//below stores {((Player)shapes[0][0]), bullets, asteroids, ufos, etc.}
	Shape[][] shapes = new Shape[][] {{new Player(new float[][] {{300, 200}, {320, 200}, {310, 240}})}, new Bullet[0], new Asteroid[0]};
	
	@Override
	public void create() {
		sr = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
	}
	
	public void collisionDetection() {
		float[][][] vertices1, vertices2; //following 3 for loops determine vertex arrays to use for collision detection
		float[][] current1, current2; //i.e. real or fake shape
		VectorLine line1, line2;
		Vec2D direction1, direction2, position1, position2;
		boolean isColliding;
		double lambda1, lambda2;
		for (int i = 0; i < shapes[2].length; i++) {
			vertices1 = shapes[2][i].getVertices(); //asteroid vertices
			for (int x = 0; x < 2; x++) {
				for (int k = 0; k < shapes[x].length; k++) {
					if (shapes[x][k].getHasExpired() == false) {
						isColliding = false;
						vertices2 = shapes[x][k].getVertices(); //player / bullet vertices
						
						for (int a = 0; a < vertices1.length; a++) { //iterates through all shapes
							for (int b = 0; b < vertices2.length; b++) {
								current1 = vertices1[a].clone();
								current2 = vertices2[b].clone();
								
								for (int c = 0; c < current1.length; c++) { //iterates through all lines making up the shapes
									for (int d = 0; d < current2.length; d++) {
										position1 = new Vec2D(current1[c][0], current1[c][1]);
										direction1 = new Vec2D(current1[(c + 1) % current1.length][0] - current1[c][0], current1[(c + 1) % current1.length][1] - current1[c][1]);
										line1 = new VectorLine(position1, direction1);
										
										position2 = new Vec2D(current2[d][0], current2[d][1]);
										direction2 = new Vec2D(current2[(d + 1) % current2.length][0] - current2[d][0], current2[(d + 1) % current2.length][1] - current2[d][1]);
										line2 = new VectorLine(position2, direction2);
										
										if (line1.findIntersection(line2) == true) {
											lambda1 = line1.getLambda1();
											lambda2 = line1.getLambda2();
											if (lambda1 >= 0 && lambda1 <= 1 && lambda2 >= 0 && lambda2 <= 1) {
												isColliding = true;
												break;
											}
											//intersection = line1.getIntersection();
										}
									}
									if (isColliding == true) {
										break;
									}
								}
								if (isColliding == true) {
									break;
								}
							}
							if (isColliding == true) {
								break;
							}
						}
						
						if (isColliding == true) {
							if (x != 0) { //destroy asteroid
								((Player)shapes[0][0]).setScore(((Player)shapes[0][0]).getScore() + 1);
								Asteroid[] append = ((Asteroid) shapes[2][i]).split(random);
								if (append.length != 0) {
									shapes[2] = splitAsteroids(shapes[2], append);
								}
								shapes[x][k].setHasExpired(true);
							}
							else { //player has lost a life
								if (((Player)shapes[0][0]).getLives() == 0) { //Game over
									Gdx.app.exit();
								}
								
								else {
									((Player)shapes[0][0]).setLives(((Player)shapes[0][0]).getLives() - 1);
									waveSize -= 2; //restarts the current wave TODO - MAYBE CHANGE
									shapes[2] = newWave();
									((Player)shapes[0][0]).setVelocity(new Vec2D(0, 0));
									((Player)shapes[0][0]).translate(new Vec2D((width / 2) - shapes[0][0].getCentre()[0], (height / 2) - shapes[0][0].getCentre()[1]));
									for (int z = 0; z < shapes[1].length; z++) {
										shapes[1][z].setHasExpired(true);
									}
								}
							}
							
							//if (x == 0) {
							//	Gdx.app.exit(); //TODO - TEMPORARY!!!
							//}
							
							break;
						}
					}
				}
			}
		}
	}
	
	public Asteroid[] splitAsteroids(Shape[] asteroids, Asteroid[] append) {
		int length = asteroids.length;
		Asteroid[] result = new Asteroid[length + 2];
		for (int i = 0; i < length; i++) {
			result[i] = ((Asteroid) asteroids[i]);
		}
		result[length] = append[0];
		result[length + 1] = append[1];
		return result;
	}
	
	public Asteroid[] newWave() {
		if (waveSize < 11) {
			waveSize += 2;
		}
		else if (waveSize > 11) {
			waveSize = 11;
		}
		
		Asteroid[] result = new Asteroid[waveSize];
		int edge;
		float relHeight, relWidth;
		double angle, magnitude;
		Vec2D currentVel;
		for (int i = 0; i < waveSize; i++) {
			edge = random.nextInt(3);
			if (edge <= 1) { //spawn on bottom / top
				relWidth = (float)(random.nextDouble() * width);
				relHeight = 0;
			}
			else { //sides
				relHeight = (float)(random.nextDouble() * height);
				relWidth = 0;
			}
			if (edge % 2 == 0) {
				relHeight = -relHeight;
				relWidth = -relWidth;
			}
			
			angle = random.nextDouble() * 2 * Math.PI;
			magnitude = (random.nextDouble() + 1) * 0.5;
			currentVel = new Vec2D(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
			result[i] = new Asteroid(asteroidVertices[random.nextInt(asteroidVertices.length)].clone(), new float[]{relWidth, relHeight}, 4, currentVel, angle);
		}
		return result;
	}
	
	public Bullet[] newBullet(Player player, Shape[] bullets) {
		int length = bullets.length;
		Bullet[] newArray = new Bullet[length + 1];
		for (int i = 0; i < length; i++) {
			newArray[i] = (Bullet)bullets[i];
		}
		float[] gunPosition = player.getVertices()[0][2];
		Vec2D direction = player.getDirection();
		float[][] vertices = new float[][] {gunPosition, {(float) (gunPosition[0] - direction.getI() * 10), (float) (gunPosition[1] - direction.getJ() * 10)}};
		newArray[length] = new Bullet(vertices, direction);
		return newArray;
	}
	
	public Shape[][] removeExpired(Shape[][] shapes) {
		Shape[][] newShapes = new Shape[shapes.length][];
		for (int i = 0; i < shapes.length; i++) {
			int count = 0;
			int length = shapes[i].length;
			for (int x = 0; x < length; x++) {
				if (shapes[i][x].getHasExpired() == true) {
					count += 1;
				}
			}
			int index = 0;
			Shape[] result = new Shape[length - count];
			for (int x = 0; x < length; x++) {
				if (shapes[i][x].getHasExpired() == false) {
					result[index] = shapes[i][x];
					index += 1;
				}
			}
			newShapes[i] = result.clone();
		}
		return newShapes;
	}
	
	public void renderShape(ShapeRenderer sr, float[][][] vertices) {
		int len;
		for (int i = 0; i < vertices.length; i++) {
			len = vertices[i].length;
			for (int x = 0; x < len; x++) {
				sr.line(vertices[i][x][0], vertices[i][x][1], vertices[i][(x + 1) % len][0], vertices[i][(x + 1) % len][1]);
			}
		}
	}
	
	public void input() {
		if (Gdx.input.isKeyPressed(Keys.A)) {
			((Player)shapes[0][0]).rotate(0.05);
			((Player)shapes[0][0]).findDirection();
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			((Player)shapes[0][0]).rotate(-0.05);
			((Player)shapes[0][0]).findDirection();
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			((Player)shapes[0][0]).applyThrust();
		}
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if (shapes[1].length < 4) {
				shapes[1] = newBullet(((Player)shapes[0][0]), shapes[1]);
			}
		}
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}
	
	public void renderText() {
		batch.begin();
		font.draw(batch, "Score: " + ((Player)shapes[0][0]).getScore(), 0, height);
		batch.end();
	}
	
	public void renderLives() {
		int offset;
		for (int i = 0; i < ((Player)shapes[0][0]).getLives(); i++) {
			offset = 30 * i;
			sr.triangle(1 + offset, 1, 20 + offset, 1, 10 + offset, 40);
		}
	}

	@Override
	public void render() { //TODO - LATER ON WHEN MORE THINGS ARE ADDED, ADD EVERYTHING TO A SINGLE ARRAY TO REDUCE CODE AMOUNT
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		input();
		renderText();
		collisionDetection();
		shapes = removeExpired(shapes);
		
		if (shapes[2].length == 0) {
			shapes[2] = newWave();
		}
		
		sr.begin(ShapeType.Line);
		renderLives();
		for (int i = 0; i < shapes.length; i++) {
			for (int x = 0; x < shapes[i].length; x++) {
				shapes[i][x].update();
				shapes[i][x].screenWrap(height, width);
				renderShape(sr, shapes[i][x].getVertices());
			}
		}
		
		sr.end();
	}
	
	@Override
	public void dispose() {
		sr.dispose();
		batch.dispose();
		font.dispose();
	}
}

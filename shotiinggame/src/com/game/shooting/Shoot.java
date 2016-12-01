package com.game.shooting;
//남이 만들어서 넣어준것 => API라고함.
import java.awt.*;
import java.awt.event.*; // KeyListener(interface)를 쓰기 위해서 필요한 import인것 같음
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*; //JFrme을 쓰기위해서 필요한것을 import 시킨것 
//class는 총 3개 
//shoot클래스
//GUI를 구현하려면 JFrame을 꼭 상속하래. 
//implements는 인터페이스를 상속받을떄 쓰이는 언어
//Runnable - 쓰레드를 만들려면 implements 해야함 (쓰레드와 관련된 인터페이스)
//KeyListener -자바에서 키를 눌렀을때 발생하는 Event로 KeyEvent를 keyListenr를 통해 컨트롤할수있음
//keylistener - 키보드에서 키 눌린거 처리할때 사용하는 듯
public class Shoot extends JFrame implements Runnable, KeyListener {
	///전역변수, 멤버변수
	//BufferedImage- 이미지클래스의 서브클래스로 이미지 데이터를 처리하고 조작하는데 사용함
	//BufferedImage의 이미지 데이터의 colormodel로 구성이 된다는데     ColorModel..?
	//BuffedIage
	private BufferedImage bi = null;	
	private ArrayList msList = null; // ArrayList타입으로 msList를 선언 - 배열타입! => 무슨용도인지는 몰라
	private ArrayList enList = null; //ArrayList 타입으로 enList를 선언 - 배열타입 => 무슨용도인지는 몰라
	// 키보드와 발사키 관련된 변수같은데 boolean type으로 false로 초기화를 준것 같음
	private boolean left = false, right = false, up = false, down = false, fire = false;
	private boolean start = false, end = false;
	// int 타입의 w, h, y, xw, wh를 선언 => 수치가나온다면 숫자를 크게키우거나 작게하거나해서 실행시켜서 어떤용도로 쓰는건지 알아봐
	//w: 창의 가로길이 h : 창의 세로 길이, x는 플레이어의시작 x좌표, y: 플레이어시작y좌표, xw와 xy는 플레이어의 가로세로크기
	private int w = 500, h = 800, x = 130, y = 450, xw = 20, xh = 20;
	//shoot()메소드 
	// 프로그램을 시작하라면 어느시점으로 시작할까?를 찾아봐 => 메인메소드부터 찾아봐
	public Shoot() { // shoot 클래스 생성자(객체를 생성할때 초기화)
		//bi에 BufferedImage 클래스를 대입
		//bufferedImage에 창의가로길이(w), 창의 세로길이(h), 
		bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		//msList와 enList의 arrayList()의 객체를 하나 생성함 
		//참조변수 - 객체의 주소를 참조할 때 쓰이는 변수
		msList = new ArrayList();
		enList = new ArrayList();
		this.addKeyListener(this); // keyListenr를 더 공부! keyListenr를 더하라..
		this.setSize(w, h); // setSize(w,h) : 창의 크기를 지정하는 것 
		this.setTitle("슈팅게임"); // 창의 제목을 정해주는 곳
		this.setResizable(false); // 창의 크기를 마음대로 바꿀수 있게끔해주는것 true=>마음대로 바꿔도 돼 false=> 고정 (대신 안에화면은 안바꿔짐)
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //java에서 스윙으로 gui를 구현하면 jframe을 상속받아서 메인프레임을 구현하는 방법이 가장 일반적으로 사용되는 방법임
		// jFraem.Exit_on_close를 지정안하면 메인프레임을 닫아도 jframe 객체는 jvm에서 계속 살아서 실행이 된다는것 
		// 내가보는 창은 종료되었지만 내부에서 계속 실행중인 상태를 말하는 것 (작업관리자 프로세스에서 java가 계속 실행되고 있음을 확인가능)
		// jframe.Exit_on_close를 지정해줘야 정상적으로 종료가 됨 		
		this.setVisible(true); // false로 바꾸면 console에 nullpointException의 예외가 발생이 나 =>예외처리잘해나  (화면을 보이느냐 안보이느냐를 결정해주는 곳인것 같음), container보이기
		
	}
	//Thread로 만들면 run이 실행이 되는 것 같음..
	public void run() {
		// try{} catch => 예외처리 문법	
		try {
			
			int msCnt = 0;
			int enCnt = 0; // 적군에 관한 수? 같을수도 있겠다..
			while (true) {// 무한루프
				Thread.sleep(8);// 쓰레기를 10동안의 시간만큼 경과되기만을 기다리는 메소드 (1000이 1초)
				//속도조절 
				/*Thread.sleep(1000);*/	// 움직임이 1000초동안 멈춰져..
				//start가 true이면  
				if (start) {
					// enCnt가 2000이상이면?  근데 enCnt가 뭐야?? 
					if (enCnt > 200) { // 적군의 수가 얼만큼 나오는가를 설정해주는것.
						enCreate(); // enCreate() 메소드를 불러옴 
						enCnt = 0;
					}
					if (msCnt >= 100) {
						fireMs();
						msCnt = 0;
					}
					msCnt += 10;
					enCnt += 10;
					keyControl();
					crashChk();
				}
				//start가 false이면 draw()실행 
				draw();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireMs() {
		if (fire) {
			if (msList.size() < 100) {
				Ms m = new Ms(this.x, this.y);
				msList.add(m);
			}
		}
	}
	//enCreate()메소드를 실행
	public void enCreate() {
		// 0부터 9미만까지 돌려 (9번까지 반복)
		for (int i = 0; i < 9; i++) {
			// Math.random()함수를  사용하네.
			double rx = Math.random() * (w - xw);
			double ry = Math.random() * 50;
			//Enemy 즉 적군에 관한 메소드라는 것을 알수있는데 en이 붙은건 적군이라고 생각할수 있음 
			//지역변수 & scope에 관해서 이론공부를......ㅣ 
			Enemy en = new Enemy((int) rx, (int) ry);
			enList.add(en);
		}
	}

	public void crashChk() {
		Graphics g = this.getGraphics(); //그래픽을 구현하는 클래스
		Polygon p = null;
		for (int i = 0; i < msList.size(); i++) {
			Ms m = (Ms) msList.get(i);
			for (int j = 0; j < enList.size(); j++) {
				Enemy e = (Enemy) enList.get(j);
				int[] xpoints = { m.x, (m.x + m.w), (m.x + m.w), m.x };
				int[] ypoints = { m.y, m.y, (m.y + m.h), (m.y + m.h) };
				p = new Polygon(xpoints, ypoints, 4);
				if (p.intersects((double) e.x, (double) e.y, (double) e.w, (double) e.h)) {
					msList.remove(i);
					enList.remove(j);
				}
			}
		}
		for (int i = 0; i < enList.size(); i++) {
			Enemy e = (Enemy) enList.get(i);
			int[] xpoints = { x, (x + xw), (x + xw), x };
			int[] ypoints = { y, y, (y + xh), (y + xh) };
			p = new Polygon(xpoints, ypoints, 4);
			if (p.intersects((double) e.x, (double) e.y, (double) e.w, (double) e.h)) {
				enList.remove(i);
				start = false;
				end = true;
			}
		}
	}

	public void draw() {
		Graphics gs = bi.getGraphics();
		gs.setColor(Color.white);
		gs.fillRect(0, 0, w, h);
		gs.setColor(Color.black);
		gs.drawString("Enemy 객체수 : " + enList.size(), 180, 50);
		gs.drawString("Ms 객체수 : " + msList.size(), 180, 70);
		gs.drawString("게임시작 : Enter", 180, 90);

		if (end) {
			gs.drawString("G A M E     O V E R", 100, 250);
		}

		gs.fillRect(x, y, xw, xh);

		for (int i = 0; i < msList.size(); i++) {
			Ms m = (Ms) msList.get(i);
			gs.setColor(Color.blue);
			gs.drawOval(m.x, m.y, m.w, m.h);
			if (m.y < 0)
				msList.remove(i);
			m.moveMs();
		}
		gs.setColor(Color.black);
		for (int i = 0; i < enList.size(); i++) {
			Enemy e = (Enemy) enList.get(i);
			gs.fillRect(e.x, e.y, e.w, e.h);
			if (e.y > h)
				enList.remove(i);
			e.moveEn();
		}

		Graphics ge = this.getGraphics();
		try{
			ge.drawImage(bi, 0, 0, w, h, this);
		}catch(Exception e){
			//java.lang.NullPointerException 예외가 생기면 실생할 코드 
			e.printStackTrace();
			System.out.println("java.lang.NullPointerException이라는 예외발생");
		}
	}

	public void keyControl() {
		if (0 < x) {
			if (left)
				x -= 3;
		}
		if (w > x + xw) {
			if (right)
				x += 3;
		}
		if (25 < y) {
			if (up)
				y -= 3;
		}
		if (h > y + xh) {
			if (down)
				y += 3;
		}
	}

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = true;
			break;
		case KeyEvent.VK_RIGHT:
			right = true;
			break;
		case KeyEvent.VK_UP:
			up = true;
			break;
		case KeyEvent.VK_DOWN:
			down = true;
			break;
		case KeyEvent.VK_A:
			fire = true;
			break;
		case KeyEvent.VK_ENTER:
			start = true;
			end = false;
			break;
		}
	}

	public void keyReleased(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		case KeyEvent.VK_UP:
			up = false;
			break;
		case KeyEvent.VK_DOWN:
			down = false;
			break;
		case KeyEvent.VK_A:
			fire = false;
			break;
		}
	}

	public void keyTyped(KeyEvent ke) {
	}
	//메인메소드 => 실행하면 제일 먼저 시작하는 곳(program의 시작점)
	public static void main(String[] args) {
		//쓰레드를 사용 => runnable가 이것때문에 사용했구나를 알수 있음...
		//쓰레드 - 프로그램의 실행 흐름, 프로그램을 구성하고 있는 실행단위라 말할수 있음
		//shoot()의 쓰레드 하나의 객체를 만듬
		//쓰레드를 왜 만드는이유는?? 몰라... 
		//강사님의 추측: shoot class를 가지고 쓰레드 만드는건가?
		Thread t = new Thread(new Shoot()); // 쓰레드하나를 생성, 
		t.start(); // 쓰레드를 시작하는 것 같음. 
	}
}

class Ms {
	int x;
	int y;
	int w = 5;
	int h = 5;

	public Ms(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void moveMs() {
		y--;
	}
}

class Enemy {
	int x;
	int y;
	int w = 10;
	int h = 10;

	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void moveEn() {
		y++;
	}
}

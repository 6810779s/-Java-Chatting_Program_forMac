
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MultiChatClient implements ActionListener, Runnable {
	private String ip;
	private String id;
	private Socket socket;
	private BufferedReader inMsg = null;
	private PrintWriter outMsg = null;

	private JPanel loginPanel;
	private JButton loginButton;
	private JLabel label1;
	private JTextField idInput;

	private JPanel logoutPanel;
	private JLabel label2;
	private JLabel label3; //접속자  
	private JButton logoutButton;

	private JPanel msgPanel;
	private JTextField msgInput;
	private JButton exitButton;

	private JFrame jframe;

	
	private Container tab;
	private CardLayout clayout;
	private Thread thread;
	private MultiChatServer clients;

	//기능 패널
	private JPanel functionPanel;
	private JButton emoticon;
	private JButton photo;
	private JRadioButton radio1 = new JRadioButton(); // 귀속말
	private JRadioButton radio2 = new JRadioButton(); // 귀속말 해제.
	private ButtonGroup group = new ButtonGroup();

	private JTextPane msgOut;

	private ImageIcon btImage;
	private ImageIcon btImage2;
	
	//이모티콘 버튼 
	private ImageIcon btemoji1;
	private ImageIcon btemoji2;
	private ImageIcon btemoji3;
	private ImageIcon btemoji4;
	private ImageIcon btemoji5;
	
	private JButton btn1;
	private JButton btn2;
	private JButton btn3;
	private JButton btn4;
	private JButton btn5;
	
	
	private JFileChooser fd;

	private JPanel emoji;
	int index; //emoticon버튼

	Image image1 = null;
	
	Image emoji1 = null;
	Image emoji2 = null;
	Image emoji3 = null;
	Image emoji4 = null;
	Image emoji5 = null;


	private JPanel panel2;


	boolean status;
	ImageIcon icon;
	ImageIcon icon2;
	ImageIcon icon3;
	ImageIcon icon4;
	ImageIcon icon5;
	ImageIcon icon6;
	ArrayList userlist = new ArrayList();

	public MultiChatClient(String ip) {

		BufferedImage img = null;

		this.ip = ip;

		JFrame jframe = new JFrame("Silver Multi Chat");
		jframe.setLayout(new BorderLayout());

		// login창,대화명 입력
		icon3 = new ImageIcon("pink.jpg");
		loginPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				// g.drawImage(icon.getImage(),0,0,null);//이미지 원래사이즈로 가져오기
				Dimension d = getSize();
				g.drawImage(icon3.getImage(), 0, 0, d.width, d.height, null); // 프레임에 맞춰서 가져오기
				setOpaque(false);
				super.paintComponent(g);
			}

		};

		idInput = new JTextField(13);
		loginButton = new JButton("로그인");
		loginButton.addActionListener(this);
		label1 = new JLabel("대화명");

		loginPanel.add(label1);
		loginPanel.add(idInput);
		loginPanel.add(loginButton);

		// 위쪽-로그인 됐을때
		icon4 = new ImageIcon("pink.jpg");
		logoutPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				// g.drawImage(icon.getImage(),0,0,null);//이미지 원래사이즈로 가져오기
				Dimension d = getSize();
				g.drawImage(icon4.getImage(), 0, 0, d.width, d.height, null); // 프레임에 맞춰서 가져오기
				setOpaque(false);
				super.paintComponent(g);
			}

		};

		logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.X_AXIS));
		label2 = new JLabel();
		label3= new JLabel("접속인원:");
		logoutButton = new JButton("로그아웃");

		logoutButton.addActionListener(this);

		logoutPanel.add(label2);

		logoutPanel.add(logoutButton);
		logoutPanel.add(label3);

		
		// emotion
		emoji = new JPanel();
		emoji.setBackground(Color.white);
		emoji.setLayout(new BoxLayout(emoji, BoxLayout.X_AXIS));
		emoji.setPreferredSize(new Dimension(30,30));
		
		//alien이모티콘
		btemoji1 = new ImageIcon("alien2.png");
		btn1 = new JButton(){
			public void paintComponent(Graphics g) {
				g.drawImage(btemoji1.getImage(), 14, 3, null);
				Dimension d = getSize();
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		btn1.addActionListener(this);
		btn1.setBorderPainted(false);
		btn1.setFocusPainted(false);
		btn1.setContentAreaFilled(false);
		btn1.setPreferredSize(new Dimension(30,10));
		
		//angry이모티콘
		btemoji2 = new ImageIcon("angryemoji2.png");
		btn2 = new JButton(){
			public void paintComponent(Graphics g) {
				g.drawImage(btemoji2.getImage(), 0, 2, null);
				Dimension d = getSize();
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		btn2.addActionListener(this);
		btn2.setBorderPainted(false);
		btn2.setFocusPainted(false);
		btn2.setContentAreaFilled(false);
		btn2.setPreferredSize(new Dimension(30,10));
		
		//animal이모티콘
		btemoji3 = new ImageIcon("animal2.png");
		btn3 = new JButton(){
			public void paintComponent(Graphics g) {
				g.drawImage(btemoji3.getImage(), 0, 3, null);
				Dimension d = getSize();
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		btn3.addActionListener(this);
		btn3.setBorderPainted(false);
		btn3.setFocusPainted(false);
		btn3.setContentAreaFilled(false);
		btn3.setPreferredSize(new Dimension(30,10));
		
		//like이모티콘
		btemoji4 = new ImageIcon("like2.png");
		btn4 = new JButton(){
			public void paintComponent(Graphics g) {
				g.drawImage(btemoji4.getImage(), 0, 2, null);
				Dimension d = getSize();
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		btn4.addActionListener(this);
		btn4.setBorderPainted(false);
		btn4.setFocusPainted(false);
		btn4.setContentAreaFilled(false);
		btn4.setPreferredSize(new Dimension(30,10));
		
		btemoji5 = new ImageIcon("smileemoji2.png");
		btn5 = new JButton(){
			public void paintComponent(Graphics g) {
				g.drawImage(btemoji5.getImage(), 0, 2, null);
				Dimension d = getSize();
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		
		btn5.addActionListener(this);
		btn5.setBorderPainted(false);
		btn5.setFocusPainted(false);
		btn5.setContentAreaFilled(false);
		btn5.setPreferredSize(new Dimension(30,10));
		
		
		emoji.add(btn1);
		emoji.add(btn2);
		emoji.add(btn3);
		emoji.add(btn4);
		emoji.add(btn5);

		// function 기능 패널
		icon6 = new ImageIcon("pink.jpg");

		functionPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				// g.drawImage(icon.getImage(),0,0,null);//이미지 원래사이즈로 가져오기
				Dimension d = getSize();
				g.drawImage(icon6.getImage(), 0, 0, d.width, d.height, null); // 프레임에 맞춰서 가져오기
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		btImage = new ImageIcon("smile.png");
		emoticon = new JButton() {
			public void paintComponent(Graphics g) {
				g.drawImage(btImage.getImage(), 30, 2, null);// 이미지 원래사이즈로 가져오기
				Dimension d = getSize();
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		emoticon.addActionListener(this);
		emoticon.setBorderPainted(false);
		emoticon.setFocusPainted(false);
		emoticon.setContentAreaFilled(false);

		btImage2 = new ImageIcon("camera.png");
		photo = new JButton() {
			public void paintComponent(Graphics g) {
				g.drawImage(btImage2.getImage(), 0, 4, null);// 이미지 원래사이즈로 가져오기
				Dimension d = getSize();
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		photo.addActionListener(this);
		photo.setBorderPainted(false);
		photo.setFocusPainted(false);
		photo.setContentAreaFilled(false);

		functionPanel.setLayout(new BoxLayout(functionPanel, BoxLayout.X_AXIS));
		radio1.setText("귓속말");
		radio2.setText("귓속말 해제");
		radio2.setSelected(true);

		group.add(radio1);
		group.add(radio2);

		functionPanel.add(radio1);
		functionPanel.add(radio2);
		functionPanel.add(emoticon);
		functionPanel.add(photo);

		// text입력 영역
		icon5 = new ImageIcon("pink.jpg");
		msgPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				g.drawImage(icon5.getImage(), 0, 0, d.width, d.height, null); // 프레임에 맞춰서 가져오기
				setOpaque(false);
				super.paintComponent(g);
			}

		};

		// 텍스트 작성 패널
		msgPanel.setLayout(new BorderLayout());
		msgInput = new JTextField();
		JScrollPane scroll = new JScrollPane(msgInput);
		scroll.setPreferredSize(new Dimension(70, 130));

		msgInput.addActionListener(this);
		exitButton = new JButton("종료");
		exitButton.addActionListener(this);

		msgPanel.add(scroll, BorderLayout.CENTER);
		msgPanel.add(exitButton, BorderLayout.EAST);
		
		//로그인,로그아웃 전환 패널
		tab = new JPanel();
		clayout = new CardLayout();
		tab.setLayout(clayout);
		tab.add(loginPanel, "login");
		tab.add(logoutPanel, "logout");

		// 텍스트 출력 영역
		msgOut = new JTextPane();
		msgOut.setPreferredSize(new Dimension(23, 360));
		msgOut.setBackground(Color.PINK);

		JScrollPane jsp = new JScrollPane(msgOut);
		jsp.setPreferredSize(new Dimension(5, 360));
		msgOut.setEditable(false);

		panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel2.add(tab);
		panel2.add(jsp);
		panel2.add(emoji);
		emoji.setVisible(false);
		panel2.add(functionPanel);
		panel2.add(msgPanel);
		

		clayout.show(tab, "login");

		// 프레임 배경
		icon = new ImageIcon("flower.jpg");
		JPanel back = new JPanel() {
			public void paintComponent(Graphics g) {
				// g.drawImage(icon.getImage(),0,0,null);//이미지 원래사이즈로 가져오기
				Dimension d = getSize();
				g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null); // 프레임에 맞춰서 가져오기
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		back.add(panel2, BorderLayout.WEST);

		jframe.add(back, BorderLayout.CENTER);
		jframe.setVisible(true);
		jframe.setSize(330, 643);
		jframe.setResizable(false);

	}

	public void connectServer() {

		try {

			socket = new Socket(ip, 8888);

			System.out.println("[Client]Server 연결 성공!!");

			System.out.println();

			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outMsg = new PrintWriter(socket.getOutputStream(), true);

			outMsg.println(id + "#" + "login");
			
			thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			System.out.println("[MultiChatClient]connectServer() Exception 발생!!");
		}
	}

	// 서버에 데이터 전송
	public void actionPerformed(ActionEvent arg0) {
		Object obj = arg0.getSource();
		if (obj == exitButton) {
			System.exit(0);
		} else if (obj == loginButton) {
			id = idInput.getText();
			label2.setText("대화명 : " + id);
			clayout.show(tab, "logout");
			connectServer();

		} else if (obj == logoutButton) {
			outMsg.println(id + "#" + "logout");
			msgOut.setText("");
			clayout.show(tab, "login");
			outMsg.close();
			try {
				inMsg.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			status = false;
		} else if (obj == msgInput) {

			if (radio1.isSelected() == true) {
				outMsg.println(id + "#" + "귓속말:" + msgInput.getText());
			} else if (radio2.isSelected() == true) {
				outMsg.println(id + "#" + msgInput.getText());
			} else {
				outMsg.println(id + "#" + msgInput.getText());

			}

			msgInput.setText("");

		} else if (obj == photo) {

			String filename;
			StyledDocument doc = msgOut.getStyledDocument();
			SimpleAttributeSet keyWord = new SimpleAttributeSet();

			fd = new JFileChooser();
			fd.setDialogTitle("전송할 이미지 선택");
			int ret = fd.showOpenDialog(null); // 파일 열기

			if (ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.", "알림", JOptionPane.WARNING_MESSAGE);
				return;
			}

			String filePath = fd.getSelectedFile().getPath();

			outMsg.println(id + "#" + "파일전송:" + filePath);

		} // else if(obj==photo)
		else if (obj == emoticon) {
			
			if (index == 0) {
				emoji.setVisible(true);
				index = 1;
			} else if (index == 1) {
				emoji.setVisible(false);
				index = 0;
			}
		}else if(obj == btn1) {
			String filePath="alien.png";
			outMsg.println(id+"#"+"alien:"+filePath);
		}else if(obj == btn2) {
			String filePath="angryemoji.png";
			outMsg.println(id+"#"+"angryemoji:"+filePath);
		}else if(obj == btn3) {
			String filePath="animal.png";
			outMsg.println(id+"#"+"animal:"+filePath);
		}else if(obj == btn4) {
			String filePath="like.png";
			outMsg.println(id+"#"+"like:"+filePath);
		}else if(obj == btn5) {
			String filePath="smileemoji.png";
			outMsg.println(id+"#"+"smileemoji:"+filePath);
		}
	}

	public void run() {

		String msg;
		String[] rmsg;
		StyledDocument doc = msgOut.getStyledDocument();
		SimpleAttributeSet keyWord = new SimpleAttributeSet();

		status = true;

		while (status) {
			try {

				msg = inMsg.readLine();
				rmsg = msg.split("#");

				if (rmsg[1].contains("바보") || rmsg[1].contains("바부") || rmsg[1].contains("멍청") || rmsg[1].contains("얼간")
						|| rmsg[1].contains("해삼")) {

					try {

						doc.insertString(doc.getLength(), rmsg[0] + ">" + rmsg[1].replace("바보", "**")
								.replace("바부", "**").replace("멍청", "**").replace("얼간", "**").replace("해삼", "**") + "\n",
								keyWord);
					} catch (Exception e) {
						System.out.println(e);
					}

				} else if (rmsg[1].contains("파일전송:")) {

					JLabel label2 = new JLabel(rmsg[0] + ">" + "\n");
					File imagesource = new File(rmsg[1].substring(5));
					image1 = ImageIO.read(imagesource);
					ImageIcon imgicon = new ImageIcon(image1);
					Image img = imgicon.getImage();
					Image changeImg = img.getScaledInstance(200, 250, Image.SCALE_SMOOTH);
					JLabel label1 = new JLabel(new ImageIcon(changeImg));

					msgOut.insertComponent(label1);
					try {
						doc.insertString(doc.getLength(), "\n", keyWord);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					msgOut.insertComponent(label2);

				}else if(rmsg[1].contains("alien:")) { //btn1
					JLabel label2 = new JLabel(rmsg[0] + ">" + "\n");
					File imagesource = new File(rmsg[1].substring(6));
					emoji1=ImageIO.read(imagesource);
					ImageIcon imgicon = new ImageIcon(emoji1);
					Image img = imgicon.getImage();
					Image changeImg = img.getScaledInstance(100, 130, Image.SCALE_SMOOTH);
					JLabel label1 = new JLabel(new ImageIcon(changeImg));
					
					msgOut.insertComponent(label1);
					try {
						doc.insertString(doc.getLength(), "\n", keyWord);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msgOut.insertComponent(label2);
					
				}else if(rmsg[1].contains("angryemoji:")) { //btn2
					JLabel label2 = new JLabel(rmsg[0] + ">" + "\n");
					File imagesource = new File(rmsg[1].substring(11));
					emoji2=ImageIO.read(imagesource);
					ImageIcon imgicon = new ImageIcon(emoji2);
					Image img = imgicon.getImage();
					Image changeImg = img.getScaledInstance(170, 130, Image.SCALE_SMOOTH);
					JLabel label1 = new JLabel(new ImageIcon(changeImg));
					
					msgOut.insertComponent(label1);
					try {
						doc.insertString(doc.getLength(), "\n", keyWord);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msgOut.insertComponent(label2);
					
				}else if(rmsg[1].contains("animal:")) { //btn3
					JLabel label2 = new JLabel(rmsg[0] + ">" + "\n");
					File imagesource = new File(rmsg[1].substring(7));
					emoji3=ImageIO.read(imagesource);
					ImageIcon imgicon = new ImageIcon(emoji3);
					Image img = imgicon.getImage();
					Image changeImg = img.getScaledInstance(100, 130, Image.SCALE_SMOOTH);
					JLabel label1 = new JLabel(new ImageIcon(changeImg));
					
					msgOut.insertComponent(label1);
					try {
						doc.insertString(doc.getLength(), "\n", keyWord);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msgOut.insertComponent(label2);
					
				}else if(rmsg[1].contains("like:")) { //btn4
					JLabel label2 = new JLabel(rmsg[0] + ">" + "\n");
					File imagesource = new File(rmsg[1].substring(5));
					emoji4=ImageIO.read(imagesource);
					ImageIcon imgicon = new ImageIcon(emoji4);
					Image img = imgicon.getImage();
					Image changeImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
					JLabel label1 = new JLabel(new ImageIcon(changeImg));
					
					msgOut.insertComponent(label1);
					try {
						doc.insertString(doc.getLength(), "\n", keyWord);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msgOut.insertComponent(label2);
					
				}else if(rmsg[1].contains("smileemoji:")) {  //btn5
					JLabel label2 = new JLabel(rmsg[0] + ">" + "\n");
					File imagesource = new File(rmsg[1].substring(11));
					emoji5=ImageIO.read(imagesource);
					ImageIcon imgicon = new ImageIcon(emoji5);
					Image img = imgicon.getImage();
					Image changeImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
					JLabel label1 = new JLabel(new ImageIcon(changeImg));
					
					msgOut.insertComponent(label1);
					try {
						doc.insertString(doc.getLength(), "\n", keyWord);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msgOut.insertComponent(label2);
					
				}else if(rmsg[0].equals("접속인원")) {
					label3.setText(rmsg[1]);
				}

				else {

					try {
						doc.insertString(doc.getLength(), rmsg[0] + ">" + rmsg[1] + "\n", keyWord);
					} catch (BadLocationException e) {

						e.printStackTrace();
					} 
				}
				msgOut.setCaretPosition(msgOut.getDocument().getLength());
			} catch (IOException e) {
				status = false;
			}
		}

		System.out.println("[MultiChatClient]" + thread.getName() + "종료됨");

	}

	public static void main(String[] args) {
		MultiChatClient mcc = new MultiChatClient("127.0.0.1");

	}
}
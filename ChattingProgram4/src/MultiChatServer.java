
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class MultiChatServer {

	private ServerSocket ss = null;
	private Socket s = null;
	ArrayList<ChatThread> chatlist = new ArrayList<ChatThread>();
	HashMap<String, Thread> map = new HashMap<>();
	

	public void start() {

		try {

			ss = new ServerSocket(8888);
			System.out.println("server start");

			while (true) {
				s = ss.accept();

				ChatThread chat = new ChatThread(s);

				chatlist.add(chat);

				chat.start();
			}
		} catch (Exception e) {

			System.out.println("[MultiChatServer]start() Exception 발생!!");
		}
	}// start()

	public static void main(String[] args) {
		MultiChatServer server = new MultiChatServer();
		server.start();
	}

	void msgSendAll(String msg) {
		for (ChatThread ct : chatlist) {
			ct.outMsg.println(msg);
			ct.outMsg.flush();
		}
	}

	public class ChatThread extends Thread { // 서버와 클라이언트가 주고 받는 데이터 저장
		Socket s;
		String msg;
		String[] rmsg;

		public ChatThread(Socket s) {
			this.s = s;

		}

		private BufferedReader inMsg = null;
		private PrintWriter outMsg = null;

		public void run() {

			boolean status = true;
			System.out.println("##ChatThread start...");

			try {

				inMsg = new BufferedReader(new InputStreamReader(s.getInputStream()));
				outMsg = new PrintWriter(s.getOutputStream(), true);

				while (status) {

					msg = inMsg.readLine();

					rmsg = msg.split("#");

					map.put(rmsg[0], Thread.currentThread());

					if (rmsg[1].equals("logout")) {
						chatlist.remove(this);
						msgSendAll("server#" + rmsg[0] + "님이 종료했습니다.");
						map.remove(rmsg[0]);
						
						msgSendAll("접속인원#" + "접속입원:" + chatlist.size());

						status = false;
					}

					else if (rmsg[1].equals("login")) {
						
						msgSendAll("Server#" + rmsg[0] + "님이 로그인했습니다.");
						msgSendAll("접속인원#" + "접속입원:" + chatlist.size());
						String getFromName = rmsg[0];

					} else if (rmsg[1].contains("파일전송:")) {
						msgSendAll(rmsg[0] + "#" + rmsg[1]);

					} else if (rmsg[1].contains("alien:")) {
						msgSendAll(rmsg[0] + "#" + rmsg[1]);
					} else if (rmsg[1].contains("angryemoji:")) {
						msgSendAll(rmsg[0] + "#" + rmsg[1]);
					} else if (rmsg[1].contains("animal:")) {
						msgSendAll(rmsg[0] + "#" + rmsg[1]);
					} else if (rmsg[1].contains("like:")) {
						msgSendAll(rmsg[0] + "#" + rmsg[1]);
					} else if (rmsg[1].contains("smileemoji:")) {
						msgSendAll(rmsg[0] + "#" + rmsg[1]);
					}

					else if (rmsg[1].contains("귓속말:")) {

						String to_user = rmsg[1].substring(4, rmsg[1].indexOf('~')); // 받는사람 닉네임.
						Thread getName = (Thread) map.get(to_user);

						// from_user=rmsg[0]
						Thread getFromName = (Thread) map.get(rmsg[0]);
						System.out.println(getFromName);
						for (ChatThread ct : chatlist) {
							if (ct.equals(getFromName)) {
								ct.outMsg.println(rmsg[0] + "#" 
							+ rmsg[1].substring(rmsg[1].indexOf('-') + 1)); 
								ct.outMsg.flush();

							}
						}
						for (ChatThread ct : chatlist) {
							if (ct.equals(getName)) {
								ct.outMsg.println(rmsg[0] + "#" 
							+ rmsg[1].substring(rmsg[1].indexOf('-') + 1));
								ct.outMsg.flush();
							}
						}
					}

					else {
						msgSendAll(msg);
					}
				}

				this.interrupt();
				System.out.println("##" + this.getName() + "stop!!");
			} catch (IOException e) {
				chatlist.remove(this);
				map.remove(rmsg[0]);
				// e.printStackTrace();
				System.out.println("[ChatThread]run() IOException 발생!!");
			}
		}
	}

}
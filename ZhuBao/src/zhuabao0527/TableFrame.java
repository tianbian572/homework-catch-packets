package zhuabao0527;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.seaglasslookandfeel.*;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

import java.sql.Timestamp;
import jpcap.*;
import jpcap.packet.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


@SuppressWarnings("deprecation")
public class TableFrame extends JFrame
{

	private JPanel contentPane;
	private JScrollPane scrollPane;//表格面板
	private JScrollPane treeScrollPane;//树的面板
	private final JTable table;//表格
	private JTextArea dataText;//数据显示窗口
	private DefaultTableModel tableModel;//表格模型
	private JComboBox<String> comboBox;//数据编码方式选择
	private JTree frameTree;//包分析结果显示树
	private JTextArea infoText;//数据报头部信息显示区
	private String encoding="utf-8";//默认数据编码方式
	
	private static NetworkInterface selectedCard=ZhuaBaoUI.getNetworkInterface();//被选择的网卡
	private static int maxPacketSize=ZhuaBaoUI.getMaxPcketSize();//捕获数据报的最大数据量
	private static boolean ifPromics=ZhuaBaoUI.getHunZa();//混杂模式选择
	private static boolean  ifCatchPacket=false;//用于控制网卡是否进行捕获
	private static String  filter=ZhuaBaoUI.getFilter() ;//过滤器
	private JTextField localMacText;//本地Mac显示区
	private JTextField localIPText;//本地IP显示区
	
	
	
	public static void zhu()
	{
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					TableFrame frame = new TableFrame();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 */
	public TableFrame() throws UnsupportedLookAndFeelException
	{
		setTitle("TCP/IP网络编程课程作业");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 933, 594);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setSize(this.getToolkit().getScreenSize());//初始时刻窗口大小
		setLocation(0,0);//设置初始时刻位置
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		 
		scrollPane = new JScrollPane();
		JPanel panel_1 = new JPanel();
		table = new JTable();
		
		scrollPane.setViewportView(table);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE))
				.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		JButton beginButton = new JButton("开始抓包");
		JButton endButton = new JButton("停止抓包");
		JButton studentsButton = new JButton("学生信息");
		
		JLabel lblNewLabel_1 = new JLabel("本机物理地址");
		
		localMacText = new JTextField();
		String macString="";
		byte[] mac_address = selectedCard.mac_address;
		for (byte b : mac_address)
			macString+=Integer.toHexString(b&0xff) + "  ";
		localMacText.setText(macString);
		localMacText.setEditable(false);
		localMacText.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("本机IP地址");
		
		localIPText = new JTextField();
		//NetworkInterfaceAddress[] aipp=selectedCard.addresses;
		for(NetworkInterfaceAddress a:selectedCard.addresses)
		{
			localIPText.setText(a.address.toString());
			//IPAddtext.
		}
		localIPText.setEditable(false);
		localIPText.setColumns(10);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(5)
					.addComponent(beginButton)
					.addGap(5)
					.addComponent(endButton)
					.addGap(5)
					.addComponent(studentsButton)
					.addGap(247)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(localMacText, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(localIPText, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
					.addGap(42))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(5)
					.addComponent(beginButton))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(5)
					.addComponent(endButton))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(studentsButton)
						.addComponent(lblNewLabel_1)
						.addComponent(localMacText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_3)
						.addComponent(localIPText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		panel_1.setLayout(gl_panel_1);
		
		studentsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				String xxString="作业小组成员：\r\n  吕    仓\r\n  张冰洋\r\n  陈    燕";
				JOptionPane.showConfirmDialog(TableFrame.this, xxString, "小组信息", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
			}
		});
		endButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) //点击停止抓包
			{
				ifCatchPacket=false;
				filter="";//停止时，清空过滤器
				//System.out.println("======抓包结束=====");
			}
		});
		beginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) //点击开始抓包按钮
			{
				//infoText.setEnabled(false);
				catchPackets();//开始SwingWorker线程抓包
			}
		});
		table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) //点击显示信息按钮
			{
				//(table.getValueAt(table.getSelectedRow(), 5));
				Packet packet=(Packet) tableModel.getValueAt(table.getSelectedRow(), 6);//数据模型位置6
				showPackets(packet);
					
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		treeScrollPane = new JScrollPane();
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(treeScrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
				.addComponent(scrollPane_2)
				.addComponent(scrollPane_1, Alignment.TRAILING)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(treeScrollPane, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
					.addGap(17)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE))
		);
		
		comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) 
			{
				encoding=comboBox.getSelectedItem().toString();
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"UTF-8", "GB2312", "US-ASCII", "ISO-8859-1", "UTF-16", "UTF-16BE", "UTF-16LE"}));
		scrollPane_1.setColumnHeaderView(comboBox);
		
		dataText = new JTextArea();
		dataText.setEditable(false);
		dataText.setLineWrap(true);
		scrollPane_1.setViewportView(dataText);
		
		JLabel lblNewLabel_2 = new JLabel("头部信息");
		scrollPane_2.setColumnHeaderView(lblNewLabel_2);
		
		infoText = new JTextArea();
		infoText.setEditable(false);
		infoText.setLineWrap(true);
		scrollPane_2.setViewportView(infoText);
		
//		frameTree = new JTree(topTreeNode);
//		treeScrollPane.setViewportView(frameTree);
		
		JLabel lblNewLabel = new JLabel("详细信息");
		treeScrollPane.setColumnHeaderView(lblNewLabel);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}
	/*================================抓包之工作线程=====================================*/
	public void catchPackets()//捕获数据报
	{
		new SwingWorker<Void, DefaultTableModel>()
		{

			@Override//后台程序
			protected Void doInBackground() throws Exception
			{
				ifCatchPacket=true;//连续抓包
				Object[] columnName = new Object[]{"序号","协议类型","捕获时间","源地址","目的地址","包大小","信息"};//表头添加到模型当中
				Object[][] tableRowInfo=new Object[][]{};//数据
				tableModel=new DefaultTableModel(tableRowInfo, columnName);//建立表格
				JpcapCaptor captor=null;
				try
				{
					captor=JpcapCaptor.openDevice(selectedCard,maxPacketSize, ifPromics, 100);
					captor.setFilter(filter, true);
				} catch (Exception exc)
				{
					System.exit(0);
				}
				int count=0;
				while(ifCatchPacket)
				{
					Packet packet=captor.getPacket();
					if(packet instanceof ARPPacket)///////////////////////////        ARP      /////////////////////////////
					{ 	
						tableModel.addRow(new Object[]{count,"ARP",
								timeStamp((packet.sec * 1000)+ (packet.usec / 1000)),
								byteArrayToString(((ARPPacket)packet).sender_hardaddr," "),
								byteArrayToString(((ARPPacket)packet).target_hardaddr," "),
								packet.len,
								//packet.toString(),
								packet});//数据添加到表格
						count++;
						
					}
					else if(packet instanceof UDPPacket)///////////////////////////      UDP       ///////////////////
					{
						UDPPacket udpPacket=(UDPPacket)packet;
						tableModel.addRow(new Object[]{count,"UDP",
								timeStamp((packet.sec * 1000)+ (packet.usec / 1000)),
								udpPacket.src_ip.toString()+"::"+Integer.toString(udpPacket.src_port),
								udpPacket.dst_ip.toString()+"::"+Integer.toString(udpPacket.dst_port),
								packet.len,
								
								packet});//数据添加到表格
						count++;
						
					}
					else if(packet instanceof TCPPacket)////////////////////////////   TCP    //////////////////////
					{
						TCPPacket tcpPacket=(TCPPacket)packet;
						tableModel.addRow(new Object[]{count,"TCP",
								timeStamp((packet.sec * 1000)+ (packet.usec / 1000)),
								tcpPacket.src_ip.toString()+"::"+Integer.toString(tcpPacket.src_port),
								tcpPacket.dst_ip.toString()+"::"+Integer.toString(tcpPacket.dst_port),
								packet.len,
								packet});//数据添加到表格
						count++;
					}
					else if(packet instanceof ICMPPacket)////////////////////////////   ICMP    //////////////////////
					{
						ICMPPacket icmpPacket=(ICMPPacket)packet;
						tableModel.addRow(new Object[]{count,"ICMP",
								timeStamp((packet.sec * 1000)+ (packet.usec / 1000)),
								icmpPacket.src_ip.toString(),
								icmpPacket.dst_ip.toString(),
								packet.len,
								packet});//数据添加到表格
						count++;
					}
					publish(tableModel);
				}// end while(ifCatchPacket)
				captor.close();
				return null;
			}//end doInBackground()
			@Override
			protected void process(java.util.List<DefaultTableModel> chunks) //中间结果被Swing的事件派发线程自动调用，更新界面
			{
				for(DefaultTableModel chunk:chunks)
					table.setModel(chunk);
			}
		}.execute();//SwingWorker
		
	}//end catchPacket
	public void showPackets(final Packet packet)//显示数据报到树中
	{
		new SwingWorker<Void, Object[]>()//返回数组，第一项分析信息，第二项放数据，第三项是帧信息
		{
			@Override
			protected Void doInBackground() throws Exception
			{
				
				if(packet instanceof ARPPacket)
				{
					ARPPacket arpPacket=(ARPPacket)packet;
					DefaultMutableTreeNode message =new DefaultMutableTreeNode("信息");
					DefaultMutableTreeNode zhen=new DefaultMutableTreeNode("帧");
					DefaultMutableTreeNode arp=new DefaultMutableTreeNode("ARP");message.add(zhen);message.add(arp);
					DefaultMutableTreeNode  zhendesMac =new DefaultMutableTreeNode("目的Mac地址："+byteArrayToString(((EthernetPacket)arpPacket.datalink).dst_mac, " "));
					DefaultMutableTreeNode zhenresMac =new DefaultMutableTreeNode("源Mac地址："+byteArrayToString(((EthernetPacket)arpPacket.datalink).src_mac, " "));
					DefaultMutableTreeNode  zhentype =new DefaultMutableTreeNode("帧类型："+((EthernetPacket)arpPacket.datalink).frametype);
					zhen.add(zhendesMac);zhen.add(zhenresMac);zhen.add(zhentype);
					/*帧结束*/
					DefaultMutableTreeNode  arpYingjian =new DefaultMutableTreeNode("硬件类型："+arpPacket.hardtype);
					DefaultMutableTreeNode  arpXieYi =new DefaultMutableTreeNode("协议类型："+arpPacket.prototype);
					DefaultMutableTreeNode  arpYingJianChang =new DefaultMutableTreeNode("硬件长度："+arpPacket.hlen);
					DefaultMutableTreeNode  arpXieYiChang =new DefaultMutableTreeNode("协议长度："+arpPacket.plen);
					DefaultMutableTreeNode  arpCaoZuo =new DefaultMutableTreeNode("操作："+arpPacket.operation);
					DefaultMutableTreeNode  arpYuanMac =new DefaultMutableTreeNode("源Mac地址："+arpPacket.getSenderHardwareAddress());
					DefaultMutableTreeNode  arpYuanIP =new DefaultMutableTreeNode("源IP地址："+arpPacket.getSenderProtocolAddress());
					DefaultMutableTreeNode  arpMuDiMac =new DefaultMutableTreeNode("目的Mac地址："+arpPacket.getTargetHardwareAddress());
					DefaultMutableTreeNode  arpMudiIP =new DefaultMutableTreeNode("目的IP地址："+arpPacket.getTargetProtocolAddress());
					
					arp.add(arpYingjian);arp.add(arpXieYi);arp.add(arpYingJianChang);
					arp.add(arpXieYiChang);arp.add(arpCaoZuo);arp.add(arpYuanMac);
					arp.add( arpYuanIP);arp.add(arpMuDiMac);arp.add(arpMudiIP);
					/*添加ARP信息结束*/
					String data=null;
					String header=byteArrayToString(arpPacket.header," ");
				
					publish(new Object[]{message,header,data});
					
				}//end if arpPacket
				else if(packet instanceof UDPPacket)
					{
						UDPPacket udpPacket=(UDPPacket)packet;
						DefaultMutableTreeNode message =new DefaultMutableTreeNode("信息");
						DefaultMutableTreeNode zhen=new DefaultMutableTreeNode("帧");
						DefaultMutableTreeNode ipBao=new DefaultMutableTreeNode("IP信息");
						DefaultMutableTreeNode udpBao=new DefaultMutableTreeNode("UDP信息");
						message.add(zhen);message.add(ipBao);message.add(udpBao);
						/*信息加入帧和IP数据报*/
						DefaultMutableTreeNode  zhendesMac =new DefaultMutableTreeNode("目的Mac地址："+byteArrayToString(((EthernetPacket)udpPacket.datalink).dst_mac, " "));
						DefaultMutableTreeNode zhenresMac =new DefaultMutableTreeNode("源Mac地址："+byteArrayToString(((EthernetPacket)udpPacket.datalink).src_mac, " "));
						DefaultMutableTreeNode  zhentype =new DefaultMutableTreeNode("帧类型："+((EthernetPacket)udpPacket.datalink).frametype);
						zhen.add(zhendesMac);zhen.add(zhenresMac);zhen.add(zhentype);
						/*帧结束*/
						DefaultMutableTreeNode  ipBanBen=new DefaultMutableTreeNode("版本号："+Integer.toHexString(udpPacket.version & 0xff));
						DefaultMutableTreeNode  ipShouBuChang=new DefaultMutableTreeNode("首部长度：");
						DefaultMutableTreeNode  ipFuWuLeiXing=new DefaultMutableTreeNode("服务类型："+udpPacket.rsv_tos);
						DefaultMutableTreeNode  ipPacketLength=new DefaultMutableTreeNode("数据报长度："+(udpPacket.length+20));//有疑问
						DefaultMutableTreeNode  ipBiaoshi=new DefaultMutableTreeNode("标识："+udpPacket.ident);
						DefaultMutableTreeNode  ipFlag=new DefaultMutableTreeNode("Flag:"+udpPacket.dont_frag+udpPacket.more_frag);
						DefaultMutableTreeNode  ipOffest=new DefaultMutableTreeNode("片偏移："+udpPacket.offset);
						DefaultMutableTreeNode  ipTTL = new DefaultMutableTreeNode("TTL:"+udpPacket.hop_limit);
						DefaultMutableTreeNode  ipUpPro= new DefaultMutableTreeNode("上层协议"+udpPacket.protocol);
						DefaultMutableTreeNode  ipChenk= new DefaultMutableTreeNode("首部校验");
						DefaultMutableTreeNode  ipYuanIP= new DefaultMutableTreeNode("源IP："+udpPacket.src_ip);
						DefaultMutableTreeNode  ipYuanMudi = new DefaultMutableTreeNode("目的IP："+udpPacket.dst_ip);
						DefaultMutableTreeNode  ipOption =new DefaultMutableTreeNode("选项："+byteArrayToString(udpPacket.option," "));
						ipBao.add(ipBanBen);
						ipBao.add(ipShouBuChang);ipBao.add(ipFuWuLeiXing);ipBao.add(ipPacketLength);
						ipBao.add(ipBiaoshi);ipBao.add(ipFlag);ipBao.add(ipOffest);
						ipBao.add( ipTTL);ipBao.add(ipUpPro);ipBao.add(ipChenk);
						ipBao.add(ipYuanIP);ipBao.add(ipYuanMudi);ipBao.add(ipOption);
						/*ip数据报添加完成*/
						DefaultMutableTreeNode  udpYuanPort= new DefaultMutableTreeNode("源端口号："+udpPacket.src_port);
						DefaultMutableTreeNode  udpMuDiPort= new DefaultMutableTreeNode("目的端口号："+udpPacket.dst_port);
						DefaultMutableTreeNode  udpChang =new DefaultMutableTreeNode("数据报长度"+udpPacket.length);
						DefaultMutableTreeNode  udpCheck =new DefaultMutableTreeNode("校验和");
						udpBao.add(udpYuanPort);udpBao.add(udpMuDiPort);udpBao.add(udpChang);udpBao.add(udpCheck);
						/*udp数据报添加完成*/
						
						String data =dataToString(udpPacket.data,encoding);
						String header=byteArrayToString(udpPacket.header, " ");
						publish(new Object[]{message,header,data});
					}//ip--UDP
						
				else if(packet instanceof TCPPacket)
					{
						TCPPacket tcpPacket=(TCPPacket)packet;
						//UDPPacket udpPacket=(UDPPacket)packet;
						DefaultMutableTreeNode message =new DefaultMutableTreeNode("信息");
						DefaultMutableTreeNode zhen=new DefaultMutableTreeNode("帧");
						DefaultMutableTreeNode ipBao=new DefaultMutableTreeNode("IP信息");
						DefaultMutableTreeNode tcpBao=new DefaultMutableTreeNode("TCP信息");
						message.add(zhen);message.add(ipBao);message.add(tcpBao);
						/*信息加入帧和IP数据报*/
						DefaultMutableTreeNode  zhendesMac =new DefaultMutableTreeNode("目的Mac地址："+byteArrayToString(((EthernetPacket)tcpPacket.datalink).dst_mac, " "));
						DefaultMutableTreeNode zhenresMac =new DefaultMutableTreeNode("源Mac地址："+byteArrayToString(((EthernetPacket)tcpPacket.datalink).src_mac, " "));
						DefaultMutableTreeNode  zhentype =new DefaultMutableTreeNode("帧类型："+((EthernetPacket)tcpPacket.datalink).frametype);
						zhen.add(zhendesMac);zhen.add(zhenresMac);zhen.add(zhentype);
						/*帧结束*/
						DefaultMutableTreeNode  ipBanBen=new DefaultMutableTreeNode("版本号："+Integer.toHexString(tcpPacket.version & 0xff));
						DefaultMutableTreeNode  ipShouBuChang=new DefaultMutableTreeNode("首部长度：");
						DefaultMutableTreeNode  ipFuWuLeiXing=new DefaultMutableTreeNode("服务类型："+tcpPacket.rsv_tos);
						DefaultMutableTreeNode  ipPacketLength=new DefaultMutableTreeNode("数据报长度："+(tcpPacket.length+20));//有疑问
						DefaultMutableTreeNode  ipBiaoshi=new DefaultMutableTreeNode("标识："+tcpPacket.ident);
						DefaultMutableTreeNode  ipFlag=new DefaultMutableTreeNode("Flag:"+tcpPacket.dont_frag+tcpPacket.more_frag);
						DefaultMutableTreeNode  ipOffest=new DefaultMutableTreeNode("片偏移："+tcpPacket.offset);
						DefaultMutableTreeNode  ipTTL = new DefaultMutableTreeNode("TTL:"+tcpPacket.hop_limit);
						DefaultMutableTreeNode  ipUpPro= new DefaultMutableTreeNode("上层协议"+tcpPacket.protocol);
						DefaultMutableTreeNode  ipChenk= new DefaultMutableTreeNode("首部校验");
						DefaultMutableTreeNode  ipYuanIP= new DefaultMutableTreeNode("源IP："+tcpPacket.src_ip);
						DefaultMutableTreeNode  ipYuanMudi = new DefaultMutableTreeNode("目的IP："+tcpPacket.dst_ip);
						DefaultMutableTreeNode  ipOption =new DefaultMutableTreeNode("选项："+byteArrayToString(tcpPacket.option," "));
						ipBao.add(ipBanBen);
						ipBao.add(ipShouBuChang);ipBao.add(ipFuWuLeiXing);ipBao.add(ipPacketLength);
						ipBao.add(ipBiaoshi);ipBao.add(ipFlag);ipBao.add(ipOffest);
						ipBao.add( ipTTL);ipBao.add(ipUpPro);ipBao.add(ipChenk);
						ipBao.add(ipYuanIP);ipBao.add(ipYuanMudi);ipBao.add(ipOption);
						/*ip数据报添加完成*/
						DefaultMutableTreeNode  tcpYuanduankou =new DefaultMutableTreeNode("源端口："+tcpPacket.src_port);
						DefaultMutableTreeNode  tcpmudiduankou =new DefaultMutableTreeNode("目的端口："+tcpPacket.dst_port);
						DefaultMutableTreeNode  tcpXuLie =new DefaultMutableTreeNode("序列号："+tcpPacket.sequence);
						DefaultMutableTreeNode  tcpQueren =new DefaultMutableTreeNode("确认号："+tcpPacket.ack_num);
						DefaultMutableTreeNode  tcpShouChang =new DefaultMutableTreeNode("首部长度：");
						//DefaultMutableTreeNode  tcpURG =new DefaultMutableTreeNode("首部长度：");
						DefaultMutableTreeNode  tcpFlag =new DefaultMutableTreeNode("Flag");
							DefaultMutableTreeNode  tcpURG =new DefaultMutableTreeNode("URG："+tcpPacket.urg);
							DefaultMutableTreeNode  tcpACK =new DefaultMutableTreeNode("ACK："+tcpPacket.urg);
							DefaultMutableTreeNode  tcpPSH =new DefaultMutableTreeNode("PSH："+tcpPacket.psh);
							DefaultMutableTreeNode  tcpRST =new DefaultMutableTreeNode("RST："+tcpPacket.rst);
							DefaultMutableTreeNode  tcpSYN =new DefaultMutableTreeNode("SYN："+tcpPacket.syn);
							DefaultMutableTreeNode  tcpFin =new DefaultMutableTreeNode("FIN："+tcpPacket.fin);
							tcpFlag.add(tcpURG);tcpFlag.add(tcpACK);tcpFlag.add(tcpPSH);
							tcpFlag.add(tcpRST);tcpFlag.add(tcpSYN);tcpFlag.add(tcpFin);/*添加标志位完成*/
						DefaultMutableTreeNode  tcpWin =new DefaultMutableTreeNode("窗口大小："+tcpPacket.window);
						DefaultMutableTreeNode  tcpCheckSum =new DefaultMutableTreeNode("校验和");
						DefaultMutableTreeNode  tcppoint =new DefaultMutableTreeNode("紧急指针："+tcpPacket.urgent_pointer);
						DefaultMutableTreeNode  tcpXuan =new DefaultMutableTreeNode("选项和填充：");
						tcpBao.add(tcpYuanduankou);tcpBao.add(tcpmudiduankou);tcpBao.add(tcpXuLie );tcpBao.add( tcpQueren);
						tcpBao.add( tcpShouChang);tcpBao.add( tcpFlag);tcpBao.add( tcpWin);
						tcpBao.add( tcpCheckSum );tcpBao.add( tcppoint);tcpBao.add( tcpXuan );
						/*tcp数据报添加完成*/

						String data =dataToString(tcpPacket.data,encoding);
						String header=byteArrayToString(tcpPacket.header, " ");
//					////////////////////暗黑//////////////////////////////
//						
//						String rex=".";
//						//System.out.println(anhei);
//						if(data.matches(rex))
//						{
//							data="我们不知道密码";
//						}
//			
//					////////////////////////////////////////////////////
						publish(new Object[]{message,header,data});
					}
					
				else if(packet instanceof ICMPPacket)
					{
						ICMPPacket icmpPacket=(ICMPPacket)packet;
						DefaultMutableTreeNode message =new DefaultMutableTreeNode("信息");
						DefaultMutableTreeNode zhen=new DefaultMutableTreeNode("帧");
						DefaultMutableTreeNode ipBao=new DefaultMutableTreeNode("ICMP信息");
						DefaultMutableTreeNode icmpBao=new DefaultMutableTreeNode("UDP信息");
						message.add(zhen);message.add(ipBao);message.add(icmpBao);
						/*信息加入帧和IP数据报*/
						DefaultMutableTreeNode  zhendesMac =new DefaultMutableTreeNode("目的Mac地址："+byteArrayToString(((EthernetPacket)icmpPacket.datalink).dst_mac, " "));
						DefaultMutableTreeNode zhenresMac =new DefaultMutableTreeNode("源Mac地址："+byteArrayToString(((EthernetPacket)icmpPacket.datalink).src_mac, " "));
						DefaultMutableTreeNode  zhentype =new DefaultMutableTreeNode("帧类型："+((EthernetPacket)icmpPacket.datalink).frametype);
						zhen.add(zhendesMac);zhen.add(zhenresMac);zhen.add(zhentype);
						/*帧结束*/
						DefaultMutableTreeNode  ipBanBen=new DefaultMutableTreeNode("版本号："+Integer.toHexString(icmpPacket.version & 0xff));
						DefaultMutableTreeNode  ipShouBuChang=new DefaultMutableTreeNode("首部长度：");
						DefaultMutableTreeNode  ipFuWuLeiXing=new DefaultMutableTreeNode("服务类型："+icmpPacket.rsv_tos);
						DefaultMutableTreeNode  ipPacketLength=new DefaultMutableTreeNode("数据报长度："+(icmpPacket.length+20));//有疑问
						DefaultMutableTreeNode  ipBiaoshi=new DefaultMutableTreeNode("标识："+icmpPacket.ident);
						DefaultMutableTreeNode  ipFlag=new DefaultMutableTreeNode("Flag:"+icmpPacket.dont_frag+icmpPacket.more_frag);
						DefaultMutableTreeNode  ipOffest=new DefaultMutableTreeNode("片偏移："+icmpPacket.offset);
						DefaultMutableTreeNode  ipTTL = new DefaultMutableTreeNode("TTL:"+icmpPacket.hop_limit);
						DefaultMutableTreeNode  ipUpPro= new DefaultMutableTreeNode("上层协议"+icmpPacket.protocol);
						DefaultMutableTreeNode  ipChenk= new DefaultMutableTreeNode("首部校验");
						DefaultMutableTreeNode  ipYuanIP= new DefaultMutableTreeNode("源IP："+icmpPacket.src_ip);
						DefaultMutableTreeNode  ipYuanMudi = new DefaultMutableTreeNode("目的IP："+icmpPacket.dst_ip);
						DefaultMutableTreeNode  ipOption =new DefaultMutableTreeNode("选项："+byteArrayToString(icmpPacket.option," "));
						ipBao.add(ipBanBen);
						ipBao.add(ipShouBuChang);ipBao.add(ipFuWuLeiXing);ipBao.add(ipPacketLength);
						ipBao.add(ipBiaoshi);ipBao.add(ipFlag);ipBao.add(ipOffest);
						ipBao.add( ipTTL);ipBao.add(ipUpPro);ipBao.add(ipChenk);
						ipBao.add(ipYuanIP);ipBao.add(ipYuanMudi);ipBao.add(ipOption);
						/*ip数据报添加完成*/
						DefaultMutableTreeNode  icmpType=new DefaultMutableTreeNode("类型："+icmpPacket.type);
						DefaultMutableTreeNode  icmpCode=new DefaultMutableTreeNode("代码："+icmpPacket.code);
						DefaultMutableTreeNode  icmpCheck=new DefaultMutableTreeNode("检验和："+icmpPacket.checksum);
						icmpBao.add(icmpType);icmpBao.add(icmpCode);icmpBao.add(icmpCheck);
						
						String data =dataToString(icmpPacket.data,encoding);
						String header=byteArrayToString(icmpPacket.header, " ");
						publish(new Object[]{message,header,data});
					}
				return null;
			}//end doInBackground
		
			@Override
			protected void process(java.util.List<Object[]> chunks) 
			{
				for(Object[] chunk:chunks)
				{
					frameTree = new JTree((DefaultMutableTreeNode)chunk[0]);
					treeScrollPane.setViewportView(frameTree);//建立树
					infoText.setText("头部信息：\r\n"+(String)chunk[1]);//添加头部
					dataText.setText("数据信息：\r\n"+(String)chunk[2]);//添加数据
				
				}
			}
		}.execute();//SwingWorker
		
	}//end show packet
	
	
	public String byteArrayToString(byte[]  bytes,String spl)//用于转换byte[]到String
	{
		String str="";
		
		try
		{
			for (byte b : bytes)
			{
//				String ca=Integer.toHexString(b & 0xff);
//				if(ca.equals("0"))
//					
				str += Integer.toHexString(b & 0xff) + spl;
			} 
		} catch (Exception e)
		{
			str=null;
		}
		return str; 
	}
	public String dataToString(byte[] bts,String encoding) throws UnsupportedEncodingException//用于数据信息
	{
		String content;
		try
		{
			content=new String(bts,encoding);
		} catch (Exception e)
		{
			return null;
		}
		return content;
	}
	
	public  String timeStamp(long time)//转换时间
	{
		Timestamp timestamp = new Timestamp(time);
		return timestamp.toString();
	}
}

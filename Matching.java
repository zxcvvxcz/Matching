import java.io.*;
import java.util.regex.*;
public class Matching
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		myHashTable<String,String> hTable = new myHashTable();

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input,hTable);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input, myHashTable<String,String> hTable)
	{
		char operation = input.charAt(0);
		switch(operation){
			case '<':
				try {
					hTable.clear();
					String fileName = input.substring(2);
					File file = new File(fileName);
					FileReader fileReader = new FileReader(file);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					String line = "";
					int i=0;
					while ((line = bufferedReader.readLine()) != null) {
						//한 줄씩 읽은 결과 line에 저장된 상태
						i++;
						int len = line.length();
						for(int j = 0; j + 6 <= len; j++){
							String subString = line.substring(j,j + 6);
							String value="(" + i + ", " + (j + 1) + ")";
							hTable.tableInsert(subString, value);
						}
					}
				}catch (IOException e){
					e.printStackTrace();
				}
				break;
			case '@':
				hTable.printValues(Integer.parseInt(input.substring(2)));
				break;
			case '?':
				try {
					String pattern = input.substring(2);
					int len = pattern.length();
					String[] dividedPattern;
					if(len > 6)
						dividedPattern = new String[len - 5];
					else
						dividedPattern = new String[1];
					dividedPattern[0] = pattern.substring(0, 6);
					AVLNode<String, String> pNodeCurr = hTable.search(dividedPattern[0]);
					if (pNodeCurr == null)	//패턴 가장 앞자리와 일치하는 문자열이 없을 때
						throw new StringNotFoundException();
					else {
						myLinkedList<String,String> pFinalList = pNodeCurr.getItemList().clone(); //가장 앞자리의 패턴과 일치하는 문자열이 있는 linked list를 복사
						AVLNode<String, String> pNodeNext;
						for (int i = 1; i < len - 5; i++) {
							dividedPattern[i] = pattern.substring(i, 6 + i);	//i번째 글자부터 6글자를 잘라낸 패턴
							pNodeNext = hTable.search(dividedPattern[i]);	//해당 패턴이 있는 노드 검색
							if (pNodeNext == null)	//해당 위치의 패턴과 일치하는 문자열이 없을 때
								throw new StringNotFoundException();
							else{  	//해당 위치의 패턴과 일치하는 문자열이 있을 때
								myLinkedList<String, String> pListNext = pNodeNext.getItemList();	//해당 패턴이 있는 AVLNode의 linked list
								Node<String> pNodePrev = pFinalList.getHead();
								for(Node<String> pNode = pFinalList.getHead().getNext();
									pNode != null; pNode = pNode.getNext()){
									if(pFinalList.isEmpty())
										throw new StringNotFoundException();
									String s = pNode.getItem();  //지금까지 패턴이 일치하는 위치 값 중 하나
									boolean matchFound = false;
									for(Node<String> locationNode = pListNext.getHead().getNext();
										locationNode != null;locationNode = locationNode.getNext()){
										String location = locationNode.getItem();	//해당 패턴의 위치 값 중 하나
										Pattern p = Pattern.compile("(?<first>[0-9]+), (?<last>[0-9]+)");
										Matcher m1 = p.matcher(location.substring(1, location.length()-1));
										Matcher m2 = p.matcher(s.substring(1, s.length()-1));
										while(m1.find() && m2.find()){	//s와 location이 적절한 위치 값 형태일 때
											if(m1.group("first").equals(m2.group("first"))
													&& Integer.parseInt(m1.group("last")) == Integer.parseInt(m2.group("last")) + i){
												matchFound = true;	//s와 location이 연속된 위치이면 matchFound에 true 할당
												break;
											}
										}
									}
									if(!matchFound){	//연속으로 일치하는 패턴이 없는 위치 값 삭제
										pNodePrev.setNext(pNode.getNext());
										pFinalList.setSize(pFinalList.size() - 1);
									}
									else
										pNodePrev = pNode;
								}
							}
						}
						if(pFinalList.isEmpty())
							throw new StringNotFoundException();
						pFinalList.print(pFinalList.getHead().getNext());
					}

					System.out.println();
					break;
				} catch (StringNotFoundException e){
					System.out.println("(0, 0)");
				}
		}
	}
}

class StringNotFoundException extends RuntimeException{
	public StringNotFoundException(){
		super();
	}
}

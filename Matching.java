import java.io.*;
import java.util.regex.*;
public class Matching
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		myHashTable<String,String> hTable = new myHashTable<>(); // 값을 저장할 hash table
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

	private static void command(String input, myHashTable<String,String> hTable) throws NullPointerException
	{
		char operation = input.charAt(0);	//명령어
		switch(operation){
			case '<':
				try {
					hTable.clear(); // 이전에 입력된 파일의 데이터 삭제
					String fileName = input.substring(2);
					File file = new File(fileName);
					FileReader fileReader = new FileReader(file);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					String line = "";
					int i=0;	//줄 번호
					while ((line = bufferedReader.readLine()) != null) {
						//한 줄씩 읽은 결과 line에 저장된 상태
						i++;
						int len = line.length();
						for(int j = 0; j + 6 <= len; j++){	//j: 시작 글자 위치
							hTable.tableInsert(line.substring(j,j + 6), "(" + i + ", " + (j + 1) + ")");	//(i, j+1) 위치부터 6글자를 추출해 해시 테이블에 넣는다.
						}
					}
				}catch (IOException e){
					e.printStackTrace();
				}
				break;
			case '@':
				hTable.printValues(Integer.parseInt(input.substring(2)));
				//해시 테이블에서 주어진 위치에 있는 값들을 출력
				break;
			case '?':
				try {
					String pattern = input.substring(2);
					int len = pattern.length();	//검색할 패턴의 전체 길이
					int j = (len - 1) / 6;		//검색할 패턴을 6글자씩 묶어서 센 길이
					AVLNode<String, String> pNodeCurr = hTable.search(pattern.substring(0, 6));
					// 패턴에 대한 검색이 처음으로 이루어지는 부분이므로 검색 결과를 기존 결과와 비교하지 않음
					myLinkedList<String,String> pFinalList = pNodeCurr.getItemList().clone();
					//원본을 훼손하지 않기 위해 패턴과 일치하는 문자열을 가진 linked list를 복사
					AVLNode<String, String> pNodeNext;
					if(len > 6) {
						for (int i = 1; i <= j; i++) {
							String dividedPattern = (i == j) ? pattern.substring(len - 6, len) : pattern.substring(6 * i, 6 * (1 + i));
							//i번째 글자부터 6글자를 잘라낸 패턴
							pNodeNext = hTable.search(dividedPattern);    //해당 패턴이 있는 노드 검색
							myLinkedList<String, String> pListNext = pNodeNext.getItemList();    //해당 패턴이 있는 AVLNode의 linked list
							Node<String> pNodePrev = pFinalList.getHead();
							Pattern p = Pattern.compile("(?<first>[0-9]+), (?<last>[0-9]+)");
							for (Node<String> pNode = pFinalList.getHead().getNext();
								 pNode != null; pNode = pNode.getNext()) {
								String s = pNode.getItem();  //지금까지 패턴이 일치하는 위치 값 중 하나
								boolean matchFound = false;
								boolean passed = false;
								for (Node<String> locationNode = pListNext.getHead().getNext();
									 locationNode != null; locationNode = locationNode.getNext()) {
									String location = locationNode.getItem();    //해당 패턴의 위치 값 중 하나
									Matcher m1 = p.matcher(location.substring(1, location.length() - 1));
									Matcher m2 = p.matcher(s.substring(1, s.length() - 1));
									while (m1.find() && m2.find()) {    //s와 location이 적절한 위치 값 형태일 때
										int k = (i == j) ? len - 6: i * 6;
										if (m1.group("first").equals(m2.group("first"))
												&& Integer.parseInt(m1.group("last")) == Integer.parseInt(m2.group("last")) + k) {
											matchFound = true;
											//s와 location이 연속된 위치이면 matchFound에 true 할당
											break;
										}
									}
									while(m1.find() && m2.find()) {
										if ((m1.group("first")).compareTo(m2.group("first")) > 0)
											passed = true;
										//줄 번호를 지나쳐 남은 위치 값 중에 맞을 수 있는 경우가 없으면
										//반복문 종료에 쓰일 passed에 true 할당
									}
									if(matchFound || passed)
										break;
								}
								if (!matchFound) {    //연속으로 일치하는 패턴이 없는 위치 값 삭제
									pNodePrev.setNext(pNode.getNext());
									pFinalList.shrinkSize();
								}
								else
									pNodePrev = pNode;
							}
						}
					}
					pFinalList.print(pFinalList.getHead().getNext());
				} catch (NullPointerException e){ //검색 결과가 없을 때 (0, 0) 출력
					System.out.println("(0, 0)");
				}
		}
	}
}

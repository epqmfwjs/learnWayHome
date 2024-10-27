package com.learnway.consult.service;

import java.util.List;

import com.learnway.consult.domain.Consultant;
import com.learnway.consult.domain.ConsultantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learnway.consult.domain.Consultant;
import com.learnway.consult.domain.ConsultantRepository;
import com.learnway.consult.domain.Memo;
import com.learnway.consult.domain.MemoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


// Counselor 멀티 로그인 테스트 파일
@RequiredArgsConstructor
@Service
public class ConsultantService  implements UserDetailsService {

	private final ConsultantRepository consultantRepository;

    private final MemoRepository memoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Consultant consultant = consultantRepository.findByConsultantId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이름의 카운셀러 없음"));

        return new ConsultantDetails(consultant);
    }
   
    
    // 상담사 정보 조회
    public Consultant getConsultantById(Long consultantId) {
        return consultantRepository.findById(consultantId)
                .orElseThrow(() -> new IllegalArgumentException("상담사를 찾을 수 없습니다."));
    }

  //-------------------------메모관련메소드---------------------------------

    //메모저장서비스
    public Memo saveMemo(Long consultantId, String memoTitle, String memoContents) {
        // 상담사 정보 가져오기
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new IllegalArgumentException("상담사를 찾을 수 없습니다. ID: " + consultantId));

        // 메모 생성 및 저장
        Memo memo = new Memo();
        memo.setMemoTitle(memoTitle);
        memo.setMemoContents(memoContents);
        memo.setConsultant(consultant);

        return memoRepository.save(memo);
    }

    //메모리스트조회 서비스
	public List<Memo> getMemoByConsultantId(Long consultantId) {
		return memoRepository.findByConsultantId(consultantId);
	}

	//메모 디테일 및 수정폼 서비스
	public List<Memo> getMemoDetail(Long memoId) {
		return memoRepository.findByMemoId(memoId);
	}


	//메모삭제 서비스
	@Transactional
	public void deleteBymemoId(Long memoId) {
		memoRepository.deleteBymemoId(memoId);
	}

	//메모수정서비스
	public Memo updateMemo(Long memoId,Long consultantId,String memoTitle, String memoContents) {


        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new IllegalArgumentException("상담사를 찾을 수 없습니다. ID: " + consultantId));


		Memo memo = new Memo();
		memo.setMemoId(memoId);
        memo.setMemoTitle(memoTitle);
        memo.setMemoContents(memoContents);
        memo.setConsultant(consultant);
		return memoRepository.save(memo);
	}
}
//--------------------------메모관련메소드 끝------------------------


package hello.community.controller.member;

import java.util.List;

import javax.validation.Valid;

import hello.community.domain.member.Member;
import hello.community.dto.member.*;
import hello.community.exception.member.MemberException;
import hello.community.global.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import hello.community.dto.subscribe.SubscribeInfoDto;
import hello.community.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/member/signup")
	public String signUpForm(@ModelAttribute MemberSignUpDto signUpDto) {

		return "member/signupForm";
	}

	@PostMapping("/member/signup")
	public String signUp(@Valid @ModelAttribute MemberSignUpDto signUpDto, BindingResult result) {

		// 검증
		if (memberService.isExistLoginId(signUpDto.getLoginId())) {
			result.rejectValue("loginId", null, "이미 사용중인 아이디 입니다");
		}
		if (memberService.isExistNickname(signUpDto.getNickname())) {
			result.rejectValue("nickname", null, "이미 사용중인 닉네임 입니다");
		}
		if (memberService.isExistEmail(signUpDto.getEmail())) {
			result.rejectValue("email", null, "이미 사용중인 이메일 입니다");
		}
		if (!signUpDto.getPassword().equals(signUpDto.getPasswordConfirm())){
			result.rejectValue("passwordConfirm", null, "패스워드가 서로 다릅니다");
		}

		if (result.hasErrors()) {
			return "member/signupForm";
		}
		
		// 성공로직
		memberService.signUp(signUpDto);

		return "redirect:/";
	}

	@GetMapping("/member/edit/info")
	public String editInfoForm(Model model){

		MemberEditDto editInfoDto = memberService.getEditInfo();

		model.addAttribute("memberEditDto", editInfoDto);

		return "/member/editInfoForm";
	}

	@PutMapping("/member/edit/info")
	public String editMember(@Valid @ModelAttribute MemberEditDto editInfoDto, BindingResult result){

		if(memberService.isEditNickname(editInfoDto.getNickname())) {
			if (memberService.isExistNickname(editInfoDto.getNickname())) {
				result.rejectValue("nickname", null, "이미 사용중인 닉네임 입니다");
			}
		}
		if(memberService.isEditEmail(editInfoDto.getEmail())) {
			if (memberService.isExistEmail(editInfoDto.getEmail())) {
				result.rejectValue("email", null, "이미 사용중인 이메일 입니다");
			}
		}

		if (result.hasErrors()) {	
			return "/member/editInfoForm";
		}

		memberService.editInfo(editInfoDto);

		return "redirect:/member/edit/info";
		
	}
	
	@GetMapping("/member/edit/password")
	public String editPassword(@ModelAttribute PasswordEditDto editPWDto) {
		
		return "/member/editPasswordForm";
	}
	
	@PutMapping("/member/edit/password")
	public String editPassword(@Valid @ModelAttribute PasswordEditDto editPWDto, BindingResult result){

		if(!memberService.matchPassword(editPWDto.getOriginPassword())){
			result.rejectValue("originPassword", null, "현재 비밀번호가 올바르지 않습니다");
		}
		if (!editPWDto.getNewPassword().equals(editPWDto.getNewPasswordConfirm())) {
			result.rejectValue("newPasswordConfirm", null, "비밀번호가 서로 다릅니다");
		}
		if (result.hasErrors()) {
			return "/member/editPasswordForm";
		}

		memberService.editPassword(editPWDto);

		return "redirect:/member/edit/info";
	}

	@GetMapping("/member/withdraw")
	public String withdrawForm(@ModelAttribute MemberWithdrawDto withdrawDto){

		return "/member/withdrawForm";
	}

	@DeleteMapping("/member/withdraw")
	public String withdraw(@ModelAttribute MemberWithdrawDto withdrawDto){

		memberService.withdraw(withdrawDto.getPasswordCheck());

		return "redirect:/";
	}

		
	@GetMapping("/member/{nickName}")
	public String viewInfoForm(@PathVariable String nickName, Model model) {
		
		MemberInfoDto memberInfoDto = memberService.getInfo(nickName);

		model.addAttribute("memberInfoDto", memberInfoDto);

		
		return "/member/infoForm";
	}

	@ResponseBody
	@GetMapping("/member/info/subscribe")
	public ResponseEntity<List<SubscribeInfoDto>> getSubscribe() {
		
		return new ResponseEntity<>(memberService.getSubscribeList(), HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("/member/id/{loginId}/exists")
	public ResponseEntity<Boolean> checkLoginId(@PathVariable String loginId) {
		return new ResponseEntity<>(memberService.isExistLoginId(loginId), HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("/member/nickname/{nickname}/exists")
	public ResponseEntity<Boolean> checkNickname(@PathVariable String nickname) {
		return new ResponseEntity<>(memberService.isExistNickname(nickname), HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("/member/email/{email}/exists")
	public ResponseEntity<Boolean> checkEmail(@PathVariable String email) {
		return new ResponseEntity<>(memberService.isExistEmail(email), HttpStatus.OK);
	}
	
}

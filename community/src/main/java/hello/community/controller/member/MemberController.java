package hello.community.controller.member;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import hello.community.dto.member.MemberInfoDto;
import hello.community.dto.member.MemberSignUpDto;
import hello.community.dto.member.PasswordEditDto;
import hello.community.dto.member.MemberEditDto;
import hello.community.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/signup")
	public String signUpForm(@ModelAttribute MemberSignUpDto signUpDto) {

		return "member/signupForm";
	}

	@PostMapping("/signup")
	public String signUp(@Valid @ModelAttribute MemberSignUpDto signUpDto, BindingResult result) {

		if (memberService.isExistLoginId(signUpDto.getLoginId())) {
			result.rejectValue("loginId", null, "이미 사용중인 아이디 입니다");
		}

		if (memberService.isExistNickname(signUpDto.getNickname())) {
			result.rejectValue("nickname", null, "이미 사용중인 닉네임 입니다");
		}

		if (memberService.isExistEmail(signUpDto.getEmail())) {
			result.rejectValue("email", null, "이미 사용중인 이메일 입니다");
		}

		if (result.hasErrors()) {
			return "member/signupForm";
		}
		// 성공로직
		memberService.signUp(signUpDto);

		return "redirect:/";
	}

	@GetMapping("/info")
	public String myInfoForm(Model model) throws Exception {
		MemberInfoDto memberInfoDto = memberService.getInfo();

		model.addAttribute("memberInfoDto", memberInfoDto);

		return "/member/info";
	}

	@GetMapping("/edit/info")
	public String editInfoForm(Model model) throws Exception {

		MemberEditDto editInfoDto = memberService.getEditInfo();

		model.addAttribute("memberEditDto", editInfoDto);

		return "/member/editInfoForm";
	}

	@PostMapping("/edit/info")
	public String editMember(@Valid @ModelAttribute MemberEditDto editInfoDto, BindingResult result)
			throws Exception {

		log.info("errors : {}", result);
		
		if (memberService.isExistNickname(editInfoDto.getNickname())) {
			result.rejectValue("nickname", null, "이미 사용중인 닉네임 입니다");
		}

		if (memberService.isExistEmail(editInfoDto.getEmail())) {
			result.rejectValue("email", null, "이미 사용중인 이메일 입니다");
		}

		if (result.hasErrors()) {	
			return "/member/editInfoForm";
		}

		memberService.editInfo(editInfoDto);

		return "redirect:/member/info";
		
	}
	
	@GetMapping("/edit/password")
	public String editPassword(@ModelAttribute PasswordEditDto editPWDto) {
		
		return "/member/editPasswordForm";
	}
	
	@PostMapping("/edit/password")
	public String editPassword(@Valid @ModelAttribute PasswordEditDto editPWDto, BindingResult result)
			throws Exception {
		
		if(!editPWDto.getNewPassword().equals(editPWDto.getNewPasswordCheck())) {
			result.reject("differ","비밀번호가 서로 다릅니다");
		}
		
		if (result.hasErrors()) {	
			return "/member/editPasswordForm";
		}

		memberService.editPassword(editPWDto);

		return "redirect:/member/info";

	}

	@ResponseBody
	@GetMapping("/id/{loginId}/exists")
	public ResponseEntity<Boolean> checkLoginId(@PathVariable String loginId) {
		return new ResponseEntity<>(memberService.isExistLoginId(loginId), HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("/nickname/{nickname}/exists")
	public ResponseEntity<Boolean> checkNickname(@PathVariable String nickname) {
		return new ResponseEntity<>(memberService.isExistNickname(nickname), HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("/email/{email}/exists")
	public ResponseEntity<Boolean> checkEmail(@PathVariable String email) {
		return new ResponseEntity<>(memberService.isExistEmail(email), HttpStatus.OK);
	}

}

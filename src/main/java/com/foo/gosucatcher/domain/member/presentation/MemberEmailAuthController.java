package com.foo.gosucatcher.domain.member.presentation;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foo.gosucatcher.domain.member.application.MemberEmailAuthService;
import com.foo.gosucatcher.domain.member.application.dto.request.MemberEmailAuthRequest;
import com.foo.gosucatcher.domain.member.application.dto.response.MemberEmailAuthResponse;
import com.foo.gosucatcher.domain.member.application.dto.response.MemberEmailSendResponse;
import com.foo.gosucatcher.global.error.ErrorCode;
import com.foo.gosucatcher.global.error.exception.InvalidValueException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@RestController
public class MemberEmailAuthController {

	private final MemberEmailAuthService memberEmailAuthService;

	@GetMapping("/signup/auth")
	public ResponseEntity<MemberEmailSendResponse> sendAuthEmail(@RequestParam String email) {
		isValidEmail(email);
		memberEmailAuthService.checkDuplicatedEmail(email);
		MemberEmailSendResponse authenticateResponse = memberEmailAuthService.sendAuthEmail(email);

		return ResponseEntity.ok(authenticateResponse);
	}

	@PostMapping("/signup/auth/email")
	public ResponseEntity<MemberEmailAuthResponse> authenticateMemberByEmail(
		@RequestBody @Validated MemberEmailAuthRequest memberEmailAuthRequest) {
		MemberEmailAuthResponse memberEmailAuthResponse = memberEmailAuthService.authenticateMemberByEmail(
			memberEmailAuthRequest);

		return ResponseEntity.ok(memberEmailAuthResponse);
	}

	private void isValidEmail(String email) {
		EmailValidator emailValidator = new EmailValidator();
		if (!emailValidator.isValid(email, null)) {
			throw new InvalidValueException(ErrorCode.INVALID_EMAIL_FORMAT);
		}
	}
}

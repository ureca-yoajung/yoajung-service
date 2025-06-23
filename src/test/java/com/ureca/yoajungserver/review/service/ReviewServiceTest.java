package com.ureca.yoajungserver.review.service;

import com.ureca.yoajungserver.chatbot.exception.BadWordDetectedException;
import com.ureca.yoajungserver.common.component.GptBadWordComponent;
import com.ureca.yoajungserver.common.component.LocalBadWordComponent;
import com.ureca.yoajungserver.plan.entity.Plan;
import com.ureca.yoajungserver.plan.repository.PlanRepository;
import com.ureca.yoajungserver.review.dto.request.ReviewCreateRequest;
import com.ureca.yoajungserver.review.dto.request.ReviewUpdateRequest;
import com.ureca.yoajungserver.review.dto.response.*;
import com.ureca.yoajungserver.review.entity.Review;
import com.ureca.yoajungserver.review.entity.ReviewLike;
import com.ureca.yoajungserver.review.exception.DuplicatedReviewLikeException;
import com.ureca.yoajungserver.review.exception.NotReviewAuthorException;
import com.ureca.yoajungserver.review.exception.ReviewAlreadyExistException;
import com.ureca.yoajungserver.review.exception.ReviewNotAllowedException;
import com.ureca.yoajungserver.review.repository.ReviewLikeRepository;
import com.ureca.yoajungserver.review.repository.ReviewRepository;
import com.ureca.yoajungserver.user.entity.User;
import com.ureca.yoajungserver.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private GptBadWordComponent gptBadWordComponent;

    @Mock
    private LocalBadWordComponent localBadWordComponent;

    @InjectMocks
    private ReviewServiceImpl reviewService;


    private final Plan plan = Plan.builder().build();
    private final Long PLAN_ID = plan.getId();
    private final String USER_EMAIL = "test@example.com";
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("로그인한 사용자 리뷰 목록 조회 - 별점 존재, 플랜 사용자")
    void reviewList_loggedInUser_withAvgStar_isPlanUser() {
        // Given
        User mockUser = User.builder().email(USER_EMAIL).name("user").plan(plan).build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        Long USER_ID = mockUser.getId();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.avgStar(PLAN_ID)).thenReturn(4.5);

        Page<ReviewListResponse> mockReviewPage = new PageImpl<>(
                Collections.singletonList(new ReviewListResponse()),
                pageable, 1);
        when(reviewRepository.findReviewList(eq(USER_ID), eq(PLAN_ID), any(Pageable.class)))
                .thenReturn(mockReviewPage);
        when(reviewRepository.isPlanUser(USER_ID, PLAN_ID)).thenReturn(true);

        // When
        ReviewPageResponse response = reviewService.reviewList(PLAN_ID, pageable);

        // Then
        assertNotNull(response);
        assertNotNull(response.getContent());
        assertEquals(4.5, response.getAvgStar());
        assertTrue(response.getIsPlanUser());
        assertTrue(response.getIsLogined());

        verify(userRepository).findByEmail(USER_EMAIL);
        verify(reviewRepository).avgStar(PLAN_ID);
        verify(reviewRepository).findReviewList(eq(USER_ID), eq(PLAN_ID), any(Pageable.class));
        verify(reviewRepository).isPlanUser(USER_ID, PLAN_ID);
    }


    @Test
    @DisplayName("로그인한 사용자 리뷰 목록 조회 - 요금제 미사용자")
    void reviewList_loggedInUser_notPlanUser() {
        // Given
        User mockUser = User.builder().email(USER_EMAIL).name("user").plan(plan).build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        Long USER_ID = mockUser.getId();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.avgStar(PLAN_ID)).thenReturn(4.0);

        Page<ReviewListResponse> mockReviewPage = new PageImpl<>(
                Collections.singletonList(new ReviewListResponse()), pageable, 1);
        when(reviewRepository.findReviewList(eq(USER_ID), eq(PLAN_ID), any(Pageable.class)))
                .thenReturn(mockReviewPage);
        when(reviewRepository.isPlanUser(USER_ID, PLAN_ID)).thenReturn(false);

        // When
        ReviewPageResponse response = reviewService.reviewList(PLAN_ID, pageable);

        // Then
        assertNotNull(response);
        assertEquals(4.0, response.getAvgStar());
        assertFalse(response.getIsPlanUser());
        assertTrue(response.getIsLogined());
    }


    @Test
    @DisplayName("비로그인 사용자 리뷰 목록 조회")
    void reviewList_notLoggedInUser() {
        // Given
        Authentication anonymous = new AnonymousAuthenticationToken(
                "key", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        SecurityContextHolder.getContext().setAuthentication(anonymous);

        when(reviewRepository.avgStar(PLAN_ID)).thenReturn(3.8);
        Page<ReviewListResponse> mockReviewPage = new PageImpl<>(
                Collections.singletonList(new ReviewListResponse()), pageable, 1);
        when(reviewRepository.findReviewList(eq(null), eq(PLAN_ID), any(Pageable.class)))
                .thenReturn(mockReviewPage);

        // When
        ReviewPageResponse response = reviewService.reviewList(PLAN_ID, pageable);

        // Then
        assertNotNull(response);
        assertEquals(3.8, response.getAvgStar());
        assertFalse(response.getIsPlanUser());
        assertFalse(response.getIsLogined());
    }


    @Test
    @DisplayName("리뷰 목록 조회 - 별점 없음")
    void reviewList_noAvgStar() {
        // Given
        User mockUser = User.builder().email(USER_EMAIL).name("user").plan(plan).build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        Long USER_ID = mockUser.getId();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.avgStar(PLAN_ID)).thenReturn(null); // 별점 없음

        Page<ReviewListResponse> mockReviewPage = new PageImpl<>(
                Collections.singletonList(new ReviewListResponse()), pageable, 1);
        when(reviewRepository.findReviewList(eq(USER_ID), eq(PLAN_ID), any(Pageable.class)))
                .thenReturn(mockReviewPage);
        when(reviewRepository.isPlanUser(USER_ID, PLAN_ID)).thenReturn(true);

        // When
        ReviewPageResponse response = reviewService.reviewList(PLAN_ID, pageable);

        // Then
        assertNotNull(response);
        assertEquals(0.0, response.getAvgStar());
        assertTrue(response.getIsPlanUser());
        assertTrue(response.getIsLogined());
    }

    @Test
    @DisplayName("리뷰 등록 - 성공")
    void insertReview_success() {
        // Given
        String content = "좋은 요금제였습니다.";
        int star = 4;

        ReviewCreateRequest request = ReviewCreateRequest.builder().star(star).content(content).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        User mockUser = User.builder().email(USER_EMAIL).name("user").plan(plan).build();
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));

        Review savedReview = Review.builder()
                .user(mockUser)
                .content(content)
                .star(star)
                .build();
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        when(planRepository.findById(PLAN_ID)).thenReturn(Optional.of(plan));

        when(gptBadWordComponent.validate(content)).thenReturn(true);
        when(localBadWordComponent.validate(content)).thenReturn(true);
        // When
        ReviewCreateResponse response = reviewService.insertReview(PLAN_ID, request);

        // Then
        assertNotNull(response);

        verify(userRepository).findByEmail(USER_EMAIL);
        verify(reviewRepository).save(any(Review.class));
    }


    @Test
    @DisplayName("리뷰 등록 - 실패 (욕설)")
    void insertReview_fail_validate() {
        // Given
        String content = "존나 비쌈";
        int star = 2;

        ReviewCreateRequest request = ReviewCreateRequest.builder().star(star).content(content).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        User mockUser = User.builder().email(USER_EMAIL).name("user").plan(plan).build();
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));
        when(planRepository.findById(PLAN_ID)).thenReturn(Optional.of(plan));

        when(gptBadWordComponent.validate(content)).thenReturn(false);

        // When & Then
        BadWordDetectedException ex = assertThrows(BadWordDetectedException.class, () -> {
            reviewService.insertReview(PLAN_ID, request);
        });
        assertEquals("욕설/모욕이 포함된 리뷰를 게시할 수 없습니다.", ex.getMessage());
    }

    @Test
    @DisplayName("리뷰 등록 - 실패 (요금제 이용자 아님)")
    void insertReview_fail_reviewNotAllowed() {
        // Given
        String content = "좋은 요금제였습니다.";
        int star = 4;

        ReviewCreateRequest request = ReviewCreateRequest.builder().star(star).content(content).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        Plan userCurrentPlan = Plan.builder().name("다른 요금제").build();
        User mockUser = User.builder().email(USER_EMAIL).name("user").plan(userCurrentPlan).build();
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));

        when(planRepository.findById(PLAN_ID)).thenReturn(Optional.of(plan));

        // When & Then
        ReviewNotAllowedException ex = assertThrows(ReviewNotAllowedException.class, () -> {
            reviewService.insertReview(PLAN_ID, request);
        });

        assertEquals("REVIEW_NOT_ALLOWED_409", ex.getMessage());

        verify(userRepository).findByEmail(USER_EMAIL);
        verify(planRepository).findById(PLAN_ID);
        verify(reviewRepository, never()).save(any(Review.class));
    }


    @Test
    @DisplayName("리뷰 등록 - 실패 (중복 리뷰)")
    void insertReview_fail_reviewAlreadyExist() {
        // Given
        String content = "좋은 요금제였습니다.";
        int star = 4;

        ReviewCreateRequest request = ReviewCreateRequest.builder().star(star).content(content).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        User mockUser = User.builder().email(USER_EMAIL).name("user").plan(plan).build();
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));


        when(planRepository.findById(PLAN_ID)).thenReturn(Optional.of(plan));

        when(gptBadWordComponent.validate(content)).thenReturn(true);
        when(localBadWordComponent.validate(content)).thenReturn(true);

        when(reviewRepository.existsByUserAndPlanAndIsDeletedFalse(mockUser, plan)).thenReturn(true);

        // When
        ReviewAlreadyExistException ex = assertThrows(ReviewAlreadyExistException.class, () -> {
            reviewService.insertReview(PLAN_ID, request);
        });

        assertEquals("ALREADY_EXIST_REVIEW_409", ex.getMessage());

        verify(reviewRepository).existsByUserAndPlanAndIsDeletedFalse(mockUser, plan);
    }



    @Test
    @DisplayName("리뷰 수정 - 성공")
    void updateReview_success() {
        // Given
        String content = "good";
        int star = 5;

        ReviewUpdateRequest request = ReviewUpdateRequest.builder().star(star).content(content).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        User mockUser = User.builder().email(USER_EMAIL).name("user").plan(plan).build();
        setEntityId(mockUser, 100L);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));

        Review savedReview = Review.builder()
                .user(mockUser)
                .content("좋은 요금제")
                .star(4)
                .build();
        setEntityId(savedReview, 1L);
        long REVIEW_ID = savedReview.getId();
        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(savedReview));

        when(gptBadWordComponent.validate(content)).thenReturn(true);
        when(localBadWordComponent.validate(content)).thenReturn(true);
        // When
        ReviewUpdateResponse response = reviewService.updateReview(REVIEW_ID, request);

        // Then
        assertNotNull(response);
        assertEquals(content, savedReview.getContent());
        assertEquals(star, savedReview.getStar());

        verify(userRepository).findByEmail(USER_EMAIL);
    }


    @Test
    @DisplayName("리뷰 수정 - 실패(리뷰 작성자 아님)")
    void updateReview_fail_NotReviewAuthor() {
        // Given
        String content = "good";
        int star = 5;

        ReviewUpdateRequest request = ReviewUpdateRequest.builder().star(star).content(content).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        User mockUser = User.builder().email(USER_EMAIL).name("user").plan(plan).build();
        setEntityId(mockUser, 100L); // 작성자 ID
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));

        User reviewAuthor = User.builder().email("other@email.com").name("author").build();
        setEntityId(reviewAuthor, 200L);
        
        Review savedReview = Review.builder()
                .user(reviewAuthor)
                .content(content)
                .star(star)
                .build();
        setEntityId(savedReview, 1L);
        long REVIEW_ID = savedReview.getId();
        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(savedReview));

        // When
        NotReviewAuthorException ex = assertThrows(NotReviewAuthorException.class, () -> {
            reviewService.updateReview(REVIEW_ID, request);
        });

        assertEquals("NOT_REVIEW_AUTHOR_409", ex.getMessage());
    }


    @Test
    @DisplayName("리뷰 수정 - 실패(욕설)")
    void updateReview_fail_validate() {
        // Given
        String content = "존나 비쌈";
        int star = 1;

        ReviewUpdateRequest request = ReviewUpdateRequest.builder().star(star).content(content).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        User mockUser = User.builder().email(USER_EMAIL).name("user").plan(plan).build();
        setEntityId(mockUser, 100L);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));

        Review savedReview = Review.builder()
                .user(mockUser)
                .content(content)
                .star(star)
                .build();
        setEntityId(savedReview, 1L);
        long REVIEW_ID = savedReview.getId();
        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(savedReview));

        when(gptBadWordComponent.validate(content)).thenReturn(false);

        // When & Then
        BadWordDetectedException ex = assertThrows(BadWordDetectedException.class, () -> {
            reviewService.updateReview(REVIEW_ID, request);
        });
        assertEquals("욕설/모욕이 포함된 리뷰를 게시할 수 없습니다.", ex.getMessage());

    }


    @Test
    @DisplayName("리뷰 삭제 - 성공")
    void deleteReview_success() {
        // Given
        long REVIEW_ID = 1L;
        String content = "삭제할 리뷰";
        int star = 5;

        User mockUser = User.builder()
                .email(USER_EMAIL)
                .name("user")
                .build();
        setEntityId(mockUser, 100L);

        Review review = Review.builder()
                .user(mockUser)
                .content(content)
                .star(star)
                .build();
        setEntityId(review, REVIEW_ID);

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(review));

        // When
        ReviewDeleteResponse response = reviewService.deleteReview(REVIEW_ID);

        // Then
        assertNotNull(response);
        assertEquals(REVIEW_ID, response.getReviewId());

        verify(userRepository).findByEmail(USER_EMAIL);
        verify(reviewRepository).findById(REVIEW_ID);
    }


    @Test
    @DisplayName("리뷰 삭제 - 실패 (리뷰 작성자 아님)")
    void deleteReview_fail_notAuthor() {
        // Given
        long REVIEW_ID = 1L;

        User reviewAuthor = User.builder()
                .email("author@example.com")
                .name("작성자")
                .build();
        setEntityId(reviewAuthor, 999L);

        Review review = Review.builder()
                .user(reviewAuthor)
                .content("내 리뷰 아님")
                .star(2)
                .build();
        setEntityId(review, REVIEW_ID);

        User loginUser = User.builder()
                .email(USER_EMAIL)
                .name("user")
                .build();
        setEntityId(loginUser, 100L);

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(loginUser));
        when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(review));

        // When & Then
        NotReviewAuthorException ex = assertThrows(NotReviewAuthorException.class, () -> {
            reviewService.deleteReview(REVIEW_ID);
        });

        assertEquals("NOT_REVIEW_AUTHOR_409", ex.getMessage());
        verify(userRepository).findByEmail(USER_EMAIL);
        verify(reviewRepository).findById(REVIEW_ID);
    }

    @Test
    @DisplayName("리뷰 좋아요 - 성공 (처음 누름)")
    void reviewLike_success_firstTime() {
        // Given
        Authentication auth = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        User mockUser = User.builder().email(USER_EMAIL).build();
        setEntityId(mockUser, 1L);

        Review review = Review.builder().user(mockUser).content("내용").star(5).build();
        setEntityId(review, 10L);

        ReviewLike reviewLike = ReviewLike.builder().user(mockUser).review(review).build();
        setEntityId(reviewLike, 100L);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));
        when(reviewLikeRepository.findByUserIdAndReviewId(1L, 10L)).thenReturn(Optional.empty());
        when(reviewLikeRepository.save(any(ReviewLike.class))).thenReturn(reviewLike);

        // When
        ReviewLikeResponse response = reviewService.reviewLike(10L);

        // Then
        assertNotNull(response);
        assertFalse(response.isDeleted());

        verify(reviewLikeRepository).save(any(ReviewLike.class));
    }


    @Test
    @DisplayName("리뷰 좋아요 - 성공 (좋아요 취소)")
    void reviewLike_success_cancelLike() {
        // Given
        Authentication auth = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        User mockUser = User.builder().email(USER_EMAIL).build();
        setEntityId(mockUser, 1L);

        Review review = Review.builder().user(mockUser).content("내용").star(5).build();
        setEntityId(review, 10L);

        ReviewLike existingLike = ReviewLike.builder().user(mockUser).review(review).build();
        setEntityId(existingLike, 100L);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));
        when(reviewLikeRepository.findByUserIdAndReviewId(1L, 10L)).thenReturn(Optional.of(existingLike));

        // When
        ReviewLikeResponse response = reviewService.reviewLike(10L);

        // Then
        assertNotNull(response);
        assertTrue(response.isDeleted());

        verify(reviewLikeRepository).delete(existingLike);
    }


    @Test
    @DisplayName("리뷰 좋아요 - 실패 (중복 좋아요 저장)")
    void reviewLike_fail_duplicatedLike() {
        // Given
        Authentication auth = new UsernamePasswordAuthenticationToken(USER_EMAIL, null, Collections.emptyList());
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        User mockUser = User.builder().email(USER_EMAIL).build();
        setEntityId(mockUser, 1L);

        Review review = Review.builder().user(mockUser).content("내용").star(5).build();
        setEntityId(review, 10L);

        ReviewLike reviewLike = ReviewLike.builder().user(mockUser).review(review).build();
        setEntityId(reviewLike, 100L);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));
        when(reviewLikeRepository.findByUserIdAndReviewId(1L, 10L)).thenReturn(Optional.empty());

        when(reviewLikeRepository.save(any(ReviewLike.class)))
                .thenThrow(new DataIntegrityViolationException("중복 저장"));

        // When & Then
        DuplicatedReviewLikeException ex = assertThrows(DuplicatedReviewLikeException.class, () -> {
            reviewService.reviewLike(10L);
        });

        assertEquals("DUPLICATED_REVIEW_LIKE_409", ex.getMessage());

        verify(reviewLikeRepository).save(any(ReviewLike.class));
    }



    private void setEntityId(Object entity, Long id) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("ID 설정 실패", e);
        }
    }
}





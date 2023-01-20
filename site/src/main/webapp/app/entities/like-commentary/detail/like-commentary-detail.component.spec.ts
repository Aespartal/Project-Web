import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LikeCommentaryDetailComponent } from './like-commentary-detail.component';

describe('LikeCommentary Management Detail Component', () => {
  let comp: LikeCommentaryDetailComponent;
  let fixture: ComponentFixture<LikeCommentaryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LikeCommentaryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ likeCommentary: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LikeCommentaryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LikeCommentaryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load likeCommentary on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.likeCommentary).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LikeImageDetailComponent } from './like-image-detail.component';

describe('LikeImage Management Detail Component', () => {
  let comp: LikeImageDetailComponent;
  let fixture: ComponentFixture<LikeImageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LikeImageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ likeImage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LikeImageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LikeImageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load likeImage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.likeImage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommentaryDetailComponent } from './commentary-detail.component';

describe('Commentary Management Detail Component', () => {
  let comp: CommentaryDetailComponent;
  let fixture: ComponentFixture<CommentaryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CommentaryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ commentary: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CommentaryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CommentaryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load commentary on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.commentary).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

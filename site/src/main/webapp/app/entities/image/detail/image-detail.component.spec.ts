import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ImageDetailComponent } from './image-detail.component';

describe('Image Management Detail Component', () => {
  let comp: ImageDetailComponent;
  let fixture: ComponentFixture<ImageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ image: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ImageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ImageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load image on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.image).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

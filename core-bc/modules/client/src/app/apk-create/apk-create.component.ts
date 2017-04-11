import {Component, OnInit, ViewChild} from "@angular/core";
import {Data} from "../model/data";
// import {state, style, triggers} from "@angular/platform-browser/animations";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Http} from "@angular/http";
import {Ao3} from "../model/ao3";
import {AbstractControl, FormBuilder, FormControl, FormGroup, NgForm, ValidatorFn, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {Vardform} from "../model/vardform";
import {Verksamhet} from "../model/verksamhet";


@Component({
  selector: 'app-apk-create',
  templateUrl: './apk-create.component.html',
  styleUrls: ['./apk-create.component.css'],
  animations: [
    trigger('slideIn', [
      state('*', style({opacity: 0})),
      state('in', style({opacity: 1, transform: 'translateX(0)', height: 'auto'})),
      state('out', style({opacity: 0, height: '0', 'margin-bottom': 0, padding: 0})),
      transition('* => *', animate('.2s ease'))
    ])
  ]
})
export class ApkCreateComponent implements OnInit {

  createApkForm: FormGroup;
  // @ViewChild('createApkForm') currentForm: NgForm;

  data: Data;

  // agarforms: Agarform[];
  ao3s: Ao3[];
  vardforms: Vardform[];
  verksamhets: Verksamhet[];

  // todo Add to Data type?
  agarform: string;//Agarform = new Agarform();

  // ao3Control = new FormControl();
  // vardformControl = new FormControl();
  // verksamhetControl = new FormControl();

  filteredAo3Options: Observable<Ao3[]>;
  filteredVardformOptions: Observable<Vardform[]>;
  filteredVerksamhetOptions: Observable<Verksamhet[]>;

  constructor(private http: Http,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {

    this.data = new Data();

    this.buildForm();

    this.http.get('/api/ao3')
      .map(response => response.json())
      .subscribe(ao3s => {
        this.ao3s = ao3s;

        this.initAo3FormControl(ao3s);
      });

    this.http.get('/api/vardform')
      .map(response => response.json())
      .subscribe(vardforms => {
        this.vardforms = vardforms;

        this.initVardformControl();
      });

    this.http.get('/api/verksamhet')
      .map(response => response.json())
      .subscribe(verksamhets => {
        this.verksamhets = verksamhets;

        this.initVerksamhetControl();
      });
  }

  private buildForm() {
    this.createApkForm = this.formBuilder.group({
      'agarform': [this.data.agarform, []],
      'ao3': [this.data.ao3],
      'ansvar': [this.data.ansvar, [Validators.required]],
      'vardform': [this.data.vardform],
      'verksamhet': [this.data.verksamhet],
      'sorteringskodProd': [this.data.sorteringskodProd],
      'benamning': [this.data.benamning],
      'externfaktura': [this.data.externfaktura]
    });
  }

  private initVerksamhetControl() {
    let verksamhetFormControl = this.createApkForm.get('verksamhet');

    this.filteredVerksamhetOptions = verksamhetFormControl.valueChanges
      .startWith(null)
      .map((verksamhet: Verksamhet) => verksamhet && typeof verksamhet === 'object' ? verksamhet.verksamhettext : verksamhet)
      .map((name: string) => name ? this.filterVerksamhet(name) : this.verksamhets.slice());
  }

  private initVardformControl() {
    let vardformFormControl = this.createApkForm.get('vardform');

    this.filteredVardformOptions = vardformFormControl.valueChanges
      .startWith(null)
      .map((vardform: Vardform) => vardform && typeof vardform === 'object' ? vardform.vardformtext : vardform)
      .map((name: string) => name ? this.filterVardform(name) : this.vardforms.slice());
  }

  private initAo3FormControl(ao3s) {
    let ao3FormControl = this.createApkForm.get('ao3');

    this.filteredAo3Options = ao3FormControl.valueChanges // Side effect
      .startWith(null)
      // .map(ao3 => ao3 && typeof ao3 === 'object' ? ao3.foretagsnamn : ao3)
      .map((name: string) => name ? this.filterAo3(name) : this.ao3s.slice());

    let map: Map<string, Ao3> = new Map();
    for (let ao3 of ao3s) {
      map.set(this.displayAo3Fn(ao3), ao3);
    }

    ao3FormControl.valueChanges.subscribe(value => {
      if (typeof value === 'string') {
        if (map.has(value)) {
          ao3FormControl.setValue(map.get(value));
        }
      }
    });

    ao3FormControl.setValidators(ao3Validator(ao3s))
  }

  filterAo3(name: string): Ao3[] {
    return this.ao3s.filter(option =>
    new RegExp(name, 'gi').test(option.foretagsnamn) ||
    new RegExp(name, 'gi').test(option.ao3id) ||
    new RegExp(name, 'gi').test(this.displayAo3Fn(option)));
  }

  filterVardform(name: string): Vardform[] {
    return this.vardforms.filter(option =>
    new RegExp(name, 'gi').test(option.vardformtext) ||
    new RegExp(name, 'gi').test(option.vardformid) ||
    new RegExp(name, 'gi').test(this.displayVardformFn(option)));
  }

  filterVerksamhet(name: string): Verksamhet[] {
    return this.verksamhets.filter(option =>
    new RegExp(name, 'gi').test(option.verksamhettext) ||
    new RegExp(name, 'gi').test(option.verksamhetid) ||
    new RegExp(name, 'gi').test(this.displayVerksamhetFn(option)));
  }

  displayAo3Fn(ao3: Ao3): string {
    return ao3 ? ao3.ao3id + ' - ' + ao3.foretagsnamn : '';
  }

  displayVardformFn(vardform: Vardform): string {
    return vardform ? vardform.vardformid + ' - ' + vardform.vardformtext : '';
  }

  displayVerksamhetFn(verksamhet: Verksamhet): string {
    return verksamhet ? verksamhet.verksamhetid + ' - ' + verksamhet.verksamhettext : '';
  }

}
/*
 @Directive({
 selector: '[validateAo3][ngModel]',
 providers: [
 { provide: NG_VALIDATORS, useExisting: forwardRef(() => Ao3Validator), multi: true }
 ]
 })
 export class Ao3Validator {

 // @Input() forbiddenName: string;


 constructor(possibleAo3s: Ao3[]) {
 debugger;
 console.log(possibleAo3s);
 }

 validateAo3(formControl: FormControl) {
 debugger;
 return false;
 }
 }*/
export function ao3Validator(ao3s: Ao3[]): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    return ao3s.indexOf(control.value) === -1 ? {'invalidName': control.value} : null;
  };
}

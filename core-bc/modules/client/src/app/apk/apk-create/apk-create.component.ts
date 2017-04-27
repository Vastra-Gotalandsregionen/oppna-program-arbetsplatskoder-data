import {Component, OnInit} from "@angular/core";
import {Data} from "../../model/data";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Headers, Http, RequestOptions} from "@angular/http";
import {Ao3} from "../../model/ao3";
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import {Vardform} from "../../model/vardform";
import {Verksamhet} from "../../model/verksamhet";
import {ErrorHandler} from "../../shared/error-handler";


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

  data: Data;

  allAo3s: Ao3[];
  allVardforms: Vardform[];
  allVerksamhets: Verksamhet[];

  // todo Add to Data type?
  agarform: string;//Agarform = new Agarform();

  filteredAo3Options: Observable<Ao3[]>;
  filteredVardformOptions: Observable<Vardform[]>;
  filteredVerksamhetOptions: Observable<Verksamhet[]>;

  ao3IdMap: Map<string, Ao3>;
  vardformIdMap: Map<string, Vardform>;
  verksamhetIdMap: Map<string, Verksamhet>;

  saveMessage: string;

  constructor(private http: Http,
              private formBuilder: FormBuilder,
              private errorHandler: ErrorHandler) {
  }

  ngOnInit() {

    this.data = new Data();

    let ao3Observable = this.http.get('/api/ao3').map(response => response.json());
    let vardformObservable = this.http.get('/api/vardform').map(response => response.json());
    let verksamhetObservable = this.http.get('/api/verksamhet').map(response => response.json());

    Observable.forkJoin([ao3Observable, vardformObservable, verksamhetObservable])
      .subscribe(result => {
        const ao3s = result[0];
        const vardforms = result[1];
        const verksamhets = result[2];

        this.allAo3s = ao3s;
        this.allVardforms = vardforms;
        this.allVerksamhets = verksamhets;

        let tempMap: Map<string, any> = new Map();
        for (let ao3 of ao3s) {
          tempMap.set(ao3.ao3id, ao3);
        }
        this.ao3IdMap = tempMap;

        tempMap = new Map();
        for (let vardform of vardforms) {
          tempMap.set(vardform.vardformid, vardform);
        }
        this.vardformIdMap = tempMap;

        tempMap = new Map();
        for (let verksamhet of verksamhets) {
          tempMap.set(verksamhet.verksamhetid, verksamhet);
        }
        this.verksamhetIdMap = tempMap;

        this.buildForm();

        this.initAo3FormControl(ao3s);
        this.initVardformControl();
        this.initVerksamhetControl();
      }, error => this.errorHandler.notifyError(error));
  }

  private buildForm() {

    this.createApkForm = this.formBuilder.group({
      'arbetsplatskod': [{value: this.data.arbetsplatskod, disabled: true}, []],
      'lankod': [this.data.lankod, [Validators.required]],
      'agarform': [this.data.agarform, []],
      'ao3': [this.ao3IdMap.get(this.data.ao3), [Validators.required]],
      'ansvar': [this.data.ansvar, [Validators.required]],
      'vardform': [this.vardformIdMap.get(this.data.vardform), [Validators.required]],
      'verksamhet': [this.verksamhetIdMap.get(this.data.verksamhet), [Validators.required]],
      'sorteringskodProd': [this.data.sorteringskodProd, [Validators.required]],
      'benamning': [this.data.benamning, [Validators.required]],
      'externfakturaGroup': this.formBuilder.group({
        'externfaktura': [this.data.externfaktura, [Validators.required]]
      }),
      'groupCodeGroup': this.formBuilder.group({
        'groupCode': ['', [Validators.required]]
      }),
      'apodosGroup': this.formBuilder.group({
        'apodos': [this.data.apodos + '', [Validators.required]]
      }),
      'vgpvGroup': this.formBuilder.group({
        'vgpv': [this.data.vgpv + '', [Validators.required]]
      })
    });

    this.createApkForm.statusChanges.subscribe(value => {
      if (value === 'VALID') {
        this.saveMessage = '';
      }
    });
  }

  private initVerksamhetControl() {
    let verksamhetFormControl = this.createApkForm.get('verksamhet');

    this.filteredVerksamhetOptions = verksamhetFormControl.valueChanges
      .startWith(null)
      .map((name: string) => name ? this.filterVerksamhet(name) : this.allVerksamhets.slice());
  }

  private initVardformControl() {
    let vardformFormControl = this.createApkForm.get('vardform');

    this.filteredVardformOptions = vardformFormControl.valueChanges
      .startWith(null)
      .map((name: string) => name ? this.filterVardform(name) : this.allVardforms.slice());
  }

  private initAo3FormControl(ao3s) {
    let ao3FormControl = this.createApkForm.get('ao3');

    this.filteredAo3Options = ao3FormControl.valueChanges // Side effect
      .startWith(null)
      .map((name: string) => name ? this.filterAo3(name) : this.allAo3s.slice());

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

  save() {
    if (!this.createApkForm.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    let data = this.data;
    let formModel = this.createApkForm.value;
    data.lankod = formModel.lankod;
    data.agarform = formModel.agarform;
    data.ao3 = (<Ao3> formModel.ao3).ao3id;
    data.ansvar = formModel.ansvar;
    data.vardform = (<Vardform> formModel.vardform).vardformid;
    data.verksamhet = (<Verksamhet> formModel.verksamhet).verksamhetid;
    data.sorteringskodProd = formModel.sorteringskodProd;
    data.benamning = formModel.benamning;
    data.externfaktura = formModel.externfakturaGroup.externfaktura;
    data.sorteringskodProd = formModel.sorteringskodProd;
    // data.groupCode = formModel.groupCode; todo Add this to model?
    data.apodos = formModel.apodosGroup.apodos;
    data.vgpv = formModel.vgpvGroup.vgpv;

    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    this.http.put('/api/data', JSON.stringify(data), options)
      .map(response => response.json())
      .subscribe((data: Data) => {
        this.data = data;
        this.buildForm();
      }, error => this.errorHandler.notifyError(error));
  }

  filterAo3(name: string): Ao3[] {
    return this.allAo3s.filter(option =>
    new RegExp(name, 'gi').test(option.foretagsnamn) ||
    new RegExp(name, 'gi').test(option.ao3id) ||
    new RegExp(name, 'gi').test(this.displayAo3Fn(option)));
  }

  filterVardform(name: string): Vardform[] {
    return this.allVardforms.filter(option =>
    new RegExp(name, 'gi').test(option.vardformtext) ||
    new RegExp(name, 'gi').test(option.vardformid) ||
    new RegExp(name, 'gi').test(this.displayVardformFn(option)));
  }

  filterVerksamhet(name: string): Verksamhet[] {
    return this.allVerksamhets.filter(option =>
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

export function ao3Validator(ao3s: Ao3[]): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    return ao3s.indexOf(control.value) === -1 ? {'invalidName': control.value} : null;
  };
}

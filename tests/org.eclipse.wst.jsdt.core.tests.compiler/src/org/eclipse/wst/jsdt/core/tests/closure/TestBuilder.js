
var suite = new Benchmark.Suite();

suite
    .add1('Primitive type', function() {
    })
    .add2('Global Object', function() {
    })
    .add3('User Object', function() {
    })
    .add4('Generics with a parameter', function() {
    })
    .add5('Generics with two parameters', function() {
    })
    .add6('Generics in Jsdoc style', function() {
    })
    .add7('Formal type union', function() {
    })
    .add8('Informal type union', function() {
    })
    .add9('Record type', function() {
    })
    .add10('Record type in generics', function() {
    })
    .add11('NUllable type', function() {
    })
    .add12('Nullable on a tail', function() {
    })
    .add13('Non nullable type', function() {
    })
    .add14('Non nullable type on a tail', function() {
    })
    .add15('Function type', function() {
    })
    .add16('Function type with no parameter', function() {
    })
    .add17('Function type with a parameter', function() {
    })
    .add18('Function type with two parameters', function() {
    })
    .add19('Function type with a return', function() {
    })
    .add20('Function type with a context type', function() {
    })
    .add21('Function type as a constructor', function() {
    })
    .add22('Function type with variable parameters', function() {
    })
    .add23('Function type with nullable or optional parameters', function() {
    })
    .add24('Function type as goog.ui.Component#forEachChild', function() {
    })
    .add25('Variable type', function() {
    })
    .add26('Optional type', function() {
    })
    .add27('All type', function() {
    })
    .add28('Unknown type', function() {
    })
    .add29('Unknown type with a keyword', function() {
    })
    .add30('Optional type with a "undefined" keyword', function() {
    })
    .on('complete', function() {
      console.log('Complete');
    })
    .run({ 'async': true });


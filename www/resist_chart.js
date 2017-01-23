/**
 * Created by rlarl on 2017-01-19.
 */
var chart = null;
var var_series = [{
    name: '가변저항',
    data: []
}];
var var_categories = ['1'];
$(function () {
    chart = Highcharts.chart('container', {

        title: {
            text: '가변저항',
            x: -20 //center
        },
        subtitle: {
            text: '가 변 저 항!!',
            x: -20
        },
        xAxis: {
            categories: var_categories
        },
        yAxis: {
            title: {
                text: '저항'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: 'ohm'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: var_series
    });
});

function insertDataResist(data) {
    var_series[0].data.push(Number(data));
    chart.update({
        char: {},
        series: var_series
    });
}
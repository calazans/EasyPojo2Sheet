#!/usr/bin/env python3
"""
Gera gráficos a partir dos resultados JMH em JSON.
Requer: pip install matplotlib pandas
"""

import json
import matplotlib.pyplot as plt
import pandas as pd
import sys
from pathlib import Path


def plot_throughput(json_file, output_dir):
    """Gera gráfico de throughput por volume de dados."""
    print("gerando grafico de throughput...")
    with open(json_file) as f:
        data = json.load(f)

    # Extrair dados
    results = []
    for benchmark in data:
        name = benchmark['benchmark'].split('.')[-1]
        params = benchmark.get('params', {})
        rows = int(params.get('rows', 0))
        score = benchmark['primaryMetric']['score']
        error = benchmark['primaryMetric']['scoreError']

        results.append({
            'Benchmark': name,
            'Rows': rows,
            'Throughput (ops/s)': score,
            'Error': error
        })

    if not results:
        print("❌ No data found for throughput plot")
        return

    df = pd.DataFrame(results)

    # Plot
    fig, ax = plt.subplots(figsize=(12, 6))

    for bench in df['Benchmark'].unique():
        subset = df[df['Benchmark'] == bench].sort_values('Rows')
        ax.errorbar(subset['Rows'], subset['Throughput (ops/s)'],
                    yerr=subset['Error'], label=bench, marker='o', capsize=5)

    ax.set_xlabel('Number of Rows', fontsize=12)
    ax.set_ylabel('Throughput (operations/second)', fontsize=12)
    ax.set_title('EasyPojo2Sheet Export Performance', fontsize=14, fontweight='bold')
    ax.legend()
    ax.grid(True, alpha=0.3)

    plt.tight_layout()
    plt.savefig(f'{output_dir}/throughput.png', dpi=300)
    print(f"✅ Saved: {output_dir}/throughput.png")
    plt.close()


def plot_comparison(json_file, output_dir):
    """Compara diferentes bibliotecas."""
    print("gerando grafico de comparacao...")
    with open(json_file) as f:
        data = json.load(f)

    results = []
    for benchmark in data:
        # Extrair nome da biblioteca do benchmark
        benchmark_name = benchmark['benchmark'].split('.')[-1]
        params = benchmark.get('params', {})
        rows = int(params.get('rows', 0))
        mode = benchmark['mode']
        score = benchmark['primaryMetric']['score']
        unit = benchmark['primaryMetric']['scoreUnit']

        results.append({
            'Library': benchmark_name,
            'Rows': rows,
            'Mode': mode,
            'Score': score,
            'Unit': unit
        })

    if not results:
        print("❌ No data found for comparison plot")
        return

    df = pd.DataFrame(results)

    # Agrupar por biblioteca e rows
    libraries = df['Library'].unique()
    rows_values = sorted(df['Rows'].unique())

    if len(libraries) == 0 or len(rows_values) == 0:
        print("❌ Insufficient data for comparison plot")
        return

    # Plot grouped bar chart
    fig, ax = plt.subplots(figsize=(14, 7))

    x_positions = range(len(rows_values))
    width = 0.8 / len(libraries)
    colors = plt.cm.Set3(range(len(libraries)))

    for i, lib in enumerate(libraries):
        lib_data = df[df['Library'] == lib].sort_values('Rows')
        scores = [lib_data[lib_data['Rows'] == r]['Score'].mean()
                  if r in lib_data['Rows'].values else 0
                  for r in rows_values]

        offset = width * (i - len(libraries) / 2 + 0.5)
        ax.bar([x + offset for x in x_positions], scores, width,
               label=lib, color=colors[i])

    ax.set_xlabel('Number of Rows', fontsize=12)
    ax.set_ylabel(f'Score ({df["Unit"].iloc[0]})', fontsize=12)
    ax.set_title('Performance Comparison Between Libraries',
                 fontsize=14, fontweight='bold')
    ax.set_xticks(x_positions)
    ax.set_xticklabels(rows_values)
    ax.legend(bbox_to_anchor=(1.05, 1), loc='upper left')
    ax.grid(True, alpha=0.3, axis='y')

    plt.tight_layout()
    plt.savefig(f'{output_dir}/comparison.png', dpi=300, bbox_inches='tight')
    print(f"✅ Saved: {output_dir}/comparison.png")
    plt.close()


def plot_memory(json_file, output_dir):
    """Gera gráfico de consumo de memória."""
    print("gerando grafico de consumo de memoria...")
    with open(json_file) as f:
        data = json.load(f)

    results = []
    for benchmark in data:
        name = benchmark['benchmark'].split('.')[-1]
        params = benchmark.get('params', {})
        rows = int(params.get('rows', 0))

        # GC profiler metrics - tentar diferentes formatos de chave
        secondary = benchmark.get('secondaryMetrics', {})

        # Procurar pela métrica de alocação de memória
        alloc_rate = 0
        for key in ['gc.alloc.rate.norm', '·gc.alloc.rate.norm', 'gc.alloc.rate', '·gc.alloc.rate']:
            if key in secondary:
                alloc_rate = secondary[key].get('score', 0)
                break

        if alloc_rate > 0:
            results.append({
                'Mode': name,
                'Rows': rows,
                'Memory (MB)': alloc_rate / (1024 * 1024) if alloc_rate > 1024 * 1024 else alloc_rate
            })

    if not results:
        print("❌ No memory data found (requires GC profiler)")
        return

    df = pd.DataFrame(results)

    # Plot
    fig, ax = plt.subplots(figsize=(12, 6))

    for mode in df['Mode'].unique():
        subset = df[df['Mode'] == mode].sort_values('Rows')
        ax.plot(subset['Rows'], subset['Memory (MB)'],
                marker='o', label=mode, linewidth=2)

    ax.set_xlabel('Number of Rows', fontsize=12)
    ax.set_ylabel('Memory Allocation (MB)', fontsize=12)
    ax.set_title('Memory Consumption Comparison',
                 fontsize=14, fontweight='bold')
    ax.legend()
    ax.grid(True, alpha=0.3)

    plt.tight_layout()
    plt.savefig(f'{output_dir}/memory.png', dpi=300)
    print(f"✅ Saved: {output_dir}/memory.png")
    plt.close()


acoes = {
    1: plot_throughput,
    2: plot_comparison,
    3: plot_memory
}

if __name__ == '__main__':
    if len(sys.argv) < 3:
        print("Uso: python visualizacao.py <benchmark_results.json> <opção>")
        print("Opções: 1 (throughput), 2 (comparison), 3 (memory)")
        sys.exit(1)

    json_file = sys.argv[1]

    if not Path(json_file).exists():
        print(f"❌ Error: File not found: {json_file}")
        sys.exit(1)

    try:
        opcao = int(sys.argv[2])
    except ValueError:
        print(f"❌ Error: Option must be an integer (1, 2, or 3), got: {sys.argv[2]}")
        sys.exit(1)

    if opcao not in acoes:
        print(f"❌ Error: Invalid option {opcao}. Choose 1, 2, or 3")
        sys.exit(1)

    output_dir = Path(json_file).parent / 'charts'
    output_dir.mkdir(exist_ok=True)

    print(f"📊 Generating charts from {json_file}...")
    try:
        acoes[opcao](json_file, output_dir)
        print("✅ All charts generated!")
    except Exception as e:
        print(f"❌ Error generating charts: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
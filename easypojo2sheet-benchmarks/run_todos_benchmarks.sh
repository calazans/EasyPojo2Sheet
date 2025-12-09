#!/bin/bash
set -e

echo "🔥 Running ALL benchmarks..."

# Build
mvn clean package -DskipTests

# Criar diretório de resultados
mkdir -p resultado/charts

# 1. Export Benchmark
echo "📊 [1/3] Running Export Benchmark..."
java -jar target/benchmarks.jar ExportacaoBenchmark \
    -rf json -rff resultado/export_benchmark.json

# 2. Comparison Benchmark
echo "📊 [2/3] Running Comparison Benchmark..."
java -jar target/benchmarks.jar ComparacaoBenchmark \
    -rf json -rff resultado/comparison_benchmark.json

# 3. Memory Benchmark
echo "📊 [3/3] Running Memory Benchmark..."
java -jar target/benchmarks.jar MemoriaBenchmark \
    -prof gc -rf json -rff resultado/memory_benchmark.json

echo "✅ All benchmarks completed!"
echo "📈 Generating visualizations..."

# Gerar gráficos
python3 scripts/visualizacao.py resultado/export_benchmark.json 1
python3 scripts/visualizacao.py resultado/comparison_benchmark.json 2
python3 scripts/visualizacao.py resultado/memory_benchmark.json 3

echo "🎉 Done! Check results/charts/"